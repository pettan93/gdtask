FROM adoptopenjdk/openjdk11-openj9:alpine-jre

COPY forbidden.txt forbidden.txt

EXPOSE 8080

ADD build/libs/sentenceGenerator-0.0.1-SNAPSHOT.jar sentenceGenerator-0.0.1-SNAPSHOT.jar

ADD src/main/resources/application-docker-postgres.properties application.properties

ENTRYPOINT ["java","-jar","/sentenceGenerator-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080