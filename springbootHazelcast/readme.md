# setup-demo Namespace

## precondition
Hazelcast Operator is deployed (on blue1 it is)

## setup the Hazelcast cluster
1. oc new-project myhztest
2. apply license: echo -n "mylicensekey" | base64 --> copy output to kube/hazelcast/secret.yaml and apply --> oc apply -f kube/hazelcast/secret.yaml
3. check security context: $( oc get project $(oc project  -q) -ojsonpath='{.metadata.annotations.openshift\.io/sa\.scc\.uid-range}' | cut -f1 -d '/' )  --> should give you a number
4. apply the id to kube/hazelcast/hazelcast-enterprize.yaml --> copy the number to securityContext.runAsUser & securityContext.fsGroup
5. apply Cluster CRD: oc apply -f kube/hazelcast/hazelcast-enterprize.yaml
6. wait until all Pods are up
7. create a route to the management center (either by cmd or UI --> go to Routes, create new, give a name, select the service)
8. check the created ServiceAccount (should be NamespaceName-hazelcast-enterprise): "oc get HazelcastEnterprise my-cluster -o yaml | grep ServiceAccount" 
9. the management center works in dev mode (LDAP integration needs to be tested... sorry I failed ;-) 
10. add the cluster config: delete the dev config --> "Add cluster config" --> clustername: my-cluster --> member Address: my-cluster-hazelcast-enterprise (the service Name) && PRESS ENTER!!!! otherwise, it will fail

## deploy openLDAP (ony for syrius so far)
1. clone the repo https://git.adcubum.com/users/amoncsek/repos/egresspoc/browse/deployopenldap.sh
2. set the correct NAMESPACE/PROJECT
3. execute "sh deployopenldap.sh"

## setup the database
1. Deploy a postgresdb
2. go to the "Developer view in OpenShift"
3. go to +Add
4. go to Database
5. select Postgres
6. Instantiate Template
7.  username: postgresadmin, password: mysecretpassword, database name: postgres // values must match the values in the file: resources/application-kube1.yaml   

## build & deploy the application
0. check service-dns entry in resources/config/hz-client.yaml the value is $SERVICENAME.$NAMESPACE.svc.cluster.local
1.  mvn clean package -DskipTests=true --> sorry no tests, since I work with hazelcast enterprise ;-/ --> AND YES.... I'TS GOOD OLD MAVEN
2. build the image: docker build -t hz .
3. tag the image: docker tag hz:latest amoahcp/hztest (amoahcp is my repo... select yours)
4. push the image: docker push amoahcp/hztest
5. deploy application: oc apply -f deployment.yaml
6. deploy application: oc apply -f service.yaml
7. create a route

access the application via: http://myroute/swagger-ui.html




