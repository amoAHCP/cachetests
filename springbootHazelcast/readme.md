run in docker

DB:
docker run  \
    --name some-postgres -p 5432:5432 \
    -e POSTGRES_PASSWORD=mysecretpassword \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v /Users/andy.moncsek/Downloads:/var/lib/postgresql/data \
    postgres
    
    
hazelcast (see src/resources/plain)    
docker build -t hz .
docker run -p 5701:5701 hz:latest