FROM tomcat:10.1-jdk17

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR into Tomcat
COPY target/ems-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ems.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
