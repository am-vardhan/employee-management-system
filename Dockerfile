From tomcat:10.1-jdk17

#Remove Default apps

Run rm -rf usr/local/tomcat/webapps/*

#copy WAR into tomcat

COPY target/ems-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ems.war

EXPOSE 8080

CMD ["catalina.sh" , "run"]
