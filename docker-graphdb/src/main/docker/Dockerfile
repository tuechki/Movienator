FROM ontotext/graphdb:8.10.1-ee

COPY maven/graphdb.license /opt/graphdb/dist/conf

RUN mkdir -p /root

COPY maven/config.ttl /root
COPY maven/statements.trig /root/statements.trig
COPY maven/connector.rq /root

ENV ELASTICSEARCH_URI=http://elasticsearch
ENV ELASTICSEARCH_PORT=9200

COPY maven/setup.sh /root
RUN chmod +x /root/setup.sh

ENTRYPOINT ["/root/setup.sh"]