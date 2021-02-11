          oc new-app \
          -e POSTGRESQL_USER=postgresadmin \
          -e POSTGRESQL_PASSWORD=mysecretPassword$ \
          -e POSTGRESQL_DATABASE=postgresdb \
          openshift/postgresql:latest