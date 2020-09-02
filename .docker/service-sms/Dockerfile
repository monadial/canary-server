ARG SBT_VERSION=1.3.6
ARG SBT_GPG_KEY=99E82A75642AC823

FROM openjdk:14-jdk-slim as sbt

ARG SBT_VERSION
ARG SBT_GPG_KEY

# Copy source files
WORKDIR /src
COPY . .

# Install sbt
RUN apt-get update \
  && apt-get install -y zip aria2 gnupg ca-certificates \
  && aria2c -s16 -x16 -k2M "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" \
  && aria2c -s16 -x16 -k2M "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz.asc" \
  && gpg --keyserver keyserver.ubuntu.com --recv-keys ${SBT_GPG_KEY} \
  && gpg --verify sbt-${SBT_VERSION}.tgz.asc sbt-${SBT_VERSION}.tgz \
  && tar xvfz sbt-${SBT_VERSION}.tgz -C /usr/local \
  && ln -s /usr/local/sbt/bin/sbt /usr/bin/sbt

# Build project
RUN rm versioning.sbt project/versioning.sbt \
  && sbt "project serviceSms; assembly"

# Set workdir
WORKDIR /out

# Copy startup script
COPY ./.docker/service-sms/start.sh ./app/start.sh

RUN mv /src/service-auth/target/scala-2.13/assembly.jar ./app/assembly.jar \
  && chmod +x ./app/start.sh \
  && chgrp -R 0 . \
  && chmod -R g=u .

FROM openjdk:14-jdk-slim

# Set workdir
WORKDIR /app

# Copy app files
COPY --from=sbt /out /

CMD ["./start.sh"]
USER 1000
