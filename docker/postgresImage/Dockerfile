FROM postgres:14-bullseye

LABEL maintainer="AQIE POC Project"

ENV POSTGRES_DB postgres
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD pocpwd

# To be able to use the PostGIS extension
ENV POSTGIS_MAJOR 3
ENV POSTGIS_VERSION 3.3.3+dfsg-1~exp1.pgdg110+1

RUN apt-get update \
      && apt-cache showpkg postgresql-$PG_MAJOR-postgis-$POSTGIS_MAJOR \
      && apt-get install -y --no-install-recommends \
           postgresql-$PG_MAJOR-postgis-$POSTGIS_MAJOR=$POSTGIS_VERSION \
           postgresql-$PG_MAJOR-postgis-$POSTGIS_MAJOR-scripts \
      && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /docker-entrypoint-initdb.d
COPY ./initdb-postgis.sh /docker-entrypoint-initdb.d/10_postgis.sh
COPY ./update-postgis.sh /usr/local/bin

# To add a script that will be run when the container is started because it is in docker-entrypoint-initdb.d
ADD aqie_groundzero.sql /docker-entrypoint-initdb.d/aqie_groundzero.sql
RUN chmod 755 /docker-entrypoint-initdb.d/aqie_groundzero.sql

# To add a script that will be run manually once the container is started.
ADD aqie_test_data.sql /aqie_test_data.sql
RUN chmod 755 /aqie_test_data.sql