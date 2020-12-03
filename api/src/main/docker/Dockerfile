FROM tomcat:9.0.22-jdk8
COPY maven/${project.artifactId}.war /usr/local/tomcat/webapps/
COPY maven/*.sh /root/
RUN chmod +x /root/movienator.sh

EXPOSE 28888

ENTRYPOINT ["/root/movienator.sh"]