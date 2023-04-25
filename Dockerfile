FROM eclipse-temurin:17-jdk

RUN apt-get update \
  && apt-get install -y ca-certificates curl git --no-install-recommends \
  && rm -rf /var/lib/apt/lists/*

# common for all images
ENV MAVEN_HOME /usr/share/maven

COPY --from=maven:3.9.1-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.1-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.1-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml


EXPOSE 5001:5001

ENV CRYPTO_ALGORITHM_CIPHER=${CRYPTO_ALGORITHM_CIPHER}
ENV CRYPTO_ALGORITHM_KEY=${CRYPTO_ALGORITHM_KEY}
ENV CRYPTO_KEY=${CRYPTO_KEY}
ENV DB_NAME=${DB_NAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV JWT_EXPIRATION=${JWT_EXPIRATION}
ENV JWT_SECRET=${JWT_SECRET}
ENV SECRET_KEY=${SECRET_KEY}
ENV SPRING_SECURITY_PASSWORD=${SPRING_SECURITY_PASSWORD}
ENV SPRING_SECURITY_USERNAME=${SPRING_SECURITY_USERNAME}

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

ARG MAVEN_VERSION=3.9.1
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]

WORKDIR .

COPY . .

CMD ["mvn", "clean", "install", "spring-boot:run"]
