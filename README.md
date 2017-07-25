# GeoIpApp

This app parses GeoIp DB and inserts data into ArangoDb

https://dev.maxmind.com/geoip/geoip2/geolite2/

Download http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.tar.gz
and point File(..) to local file location

But first run ArangoDb (e.g. in Docker)
docker run -e ARANGO_NO_AUTH=1 -p 8529:8529 arangodb
