# Using official tomcat image
FROM tomcat:10.0

# Copy WAR-application to Tomcat's wep-applications directory
COPY target/weatherApplication-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Remove the standard ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

