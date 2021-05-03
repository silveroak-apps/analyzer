### Analyzer is a Crypot analysis bot

The main purpose of this project is to analyze the coin technical analysis based on the ticker information available

To build and install this project

````sh
mvn clean install
````

#To run this bot with 500mb min and 2GB max memory java arguments

````sh
nohup java -jar -server -Dapp.props=/opt/buyBot/CryptoBot/app.properties -XX:MetaspaceSize=100m -Xms500m -Xmx2g -XX:NewSize=200m -XX:MaxNewSize=300m -XX:+UseG1GC -XX:+UseStringDeduplication /opt/buyBot/CryptoBot/target/analyzer-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
````

### applicationContext.xml:

Make sure the URL,username and password are appropriate. 
- analysisSource is where the analysis data going to store. 
- tickerSource is where the ticker date is stored. (readonly user should be fine)

```xml
<bean id="analysisSource" class="org.apache.commons.dbcp.BasicDataSource" primary="true">
      <property name="driverClassName" value="org.postgresql.Driver"/>
      <property name="url" value="jdbc:postgresql://10.1.1.15:5432/analysis"/>
      <property name="username" value="<username>"/>
      <property name="password" value="<password>"/>
      <property name="initialSize" value="5"/>
      <property name="maxActive" value="10"/>
  </bean>

  <bean id="tickerSource" class="org.apache.commons.dbcp.BasicDataSource">
      <property name="driverClassName" value="org.postgresql.Driver"/>
      <property name="url" value="jdbc:postgresql://localhost:5432/ticker"/>
      <property name="username" value="<username"/>
      <property name="password" value="<password>"/>
      <property name="initialSize" value="5"/>
      <property name="maxActive" value="10"/>
  </bean>

```
