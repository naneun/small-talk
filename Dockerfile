FROM openjdk:11-jdk
ARG OUTPUT=./build/libs/*.jar
COPY ${OUTPUT} small-talk.jar
ENTRYPOINT ["java","-jar","/small-talk.jar"]
