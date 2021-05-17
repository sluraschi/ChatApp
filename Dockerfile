FROM gradle:5.5.1-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app
RUN mkdir /app/db

ADD /db/initdb.sql /app/db/
COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/app.jar

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y sqlite3
RUN /usr/bin/sqlite3 /app/db/chatapp.db < /app/db/initdb.sql

ENV AuthKey="oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w"
ENV issuer="Chat-challenge-issuer"
ENV salt="hashingSalt"
ENV dbAddress="app/db/chatapp.db"

ENTRYPOINT ["java", "-jar","/app/app.jar"]