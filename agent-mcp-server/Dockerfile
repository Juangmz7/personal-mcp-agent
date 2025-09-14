FROM openjdk:22-jdk
ADD target/mcp-server-agent.jar mcp-server-agent.jar
ENTRYPOINT ["java", "-jar", "/mcp-server-agent.jar"]
EXPOSE 8081