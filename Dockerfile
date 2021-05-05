FROM amazonlinux:2 as installer
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
RUN yum update -y \
  && yum install -y unzip \
  && unzip awscliv2.zip \
  # The --bin-dir is specified so that we can copy the
  # entire bin directory from the installer stage into
  # into /usr/local/bin of the final stage without
  # accidentally copying over any other executables that
  # may be present in /usr/local/bin of the installer stage.
  && ./aws/install --bin-dir /aws-cli-bin/

FROM adoptopenjdk/openjdk15

COPY --from=installer /usr/local/aws-cli/ /usr/local/aws-cli/
COPY --from=installer /aws-cli-bin/ /usr/local/bin/

WORKDIR /app
COPY target/analyzer-1.0-SNAPSHOT.jar /app
COPY target/dependency-jars /app/dependency-jars
COPY src/env/ /app
COPY cloudformation_sqs_api.yaml /app/cloudformation_sqs_api.yaml
COPY run_java.sh /app/run_java.sh
COPY healthcheck.sh /app/healthcheck.sh

RUN chmod +x /usr/local/bin/aws
RUN chmod +x /app/run_java.sh
RUN chmod +x /app/healthcheck.sh
CMD /app/run_java.sh ${signalqueue}

EXPOSE 8090

HEALTHCHECK --start-period=15s --interval=5s --retries=50 CMD  bash -c '/app/healthcheck.sh ||  kill 1' || exit 1