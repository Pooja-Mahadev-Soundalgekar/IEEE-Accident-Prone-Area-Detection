FROM logstash

VOLUME /config
VOLUME /logs


RUN plugin install logstash-output-redis
RUN plugin install logstash-input-redis
RUN plugin install logstash-input-heartbeat

CMD ["logstash", "agent"]

