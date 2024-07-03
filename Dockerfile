FROM openjdk:17

COPY target/*.jar bot.jar

ENTRYPOINT ["java", "-jar", "bot.jar"]
