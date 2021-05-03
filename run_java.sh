#!/bin/bash

mv /app/applicationContext_$1.xml /app/applicationContext.xml
mv /app/app_$1.properties /app/app.properties
jar uf analyzer-1.0-SNAPSHOT.jar /app/applicationContext.xml

aws sqs list-queues > /app/allqueues.json
if grep -q $2 /app/allqueues.json;
then
    echo "Queue $2 found in aws account"
else
    echo "Creating queue $2"
    # shellcheck disable=SC2209
    # shellcheck disable=SC2046
    # shellcheck disable=SC2039
    # shellcheck disable=SC2005
    echo $(sed 's/[^[:alnum:]]//g' <<< "$2")
    # shellcheck disable=SC2039
    stackname=$(sed 's/[^[:alnum:]]//g' <<< "$2")
    ${stackname}
    # shellcheck disable=SC2016
    perl -pi -e 's/ROLE__NAME/'"$stackname"'/g' /app/cloudformation_sqs_api.yaml
    aws cloudformation create-stack \
    --stack-name "${stackname}" \
    --template-body file:///app/cloudformation_sqs_api.yaml \
    --parameters ParameterKey=queueName,ParameterValue="$2" \
    --capabilities CAPABILITY_NAMED_IAM
fi
java --enable-preview -jar -server -Dapp.props=/app/app.properties -XX:MetaspaceSize=100m -Xms128m -Xmx512m -XX:NewSize=200m -XX:MaxNewSize=300m -XX:+UseG1GC -XX:+UseStringDeduplication /app/analyzer-1.0-SNAPSHOT.jar $2