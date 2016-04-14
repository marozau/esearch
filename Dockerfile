FROM java:8-jre

ENV APP_DIR "/opt/esearch"
ENV LOG_DIR "/var/log/esearch"

COPY ${PWD}/targer/esearch ${APP_DIR}

WORKDIR ${APP_DIR}
RUN chmod +x index-pumper.sh &&\
    mkdir ${LOG_DIR} &&\
    chmod 600 /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/management/jmxremote.password

VOLUME ["${LOG_DIR}"]

CMD ["./index-pumper.sh"]
