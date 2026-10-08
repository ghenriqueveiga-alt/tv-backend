FROM gradle:jdk25 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:25-jre
WORKDIR /app
# O banco guarda caminhos absolutos "F:/..."; em Linux eles viram caminho
# relativo ao CWD, entao o symlink /app/F: -> /media (F:\ montado) os resolve.
RUN ln -s /media /app/F:
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
