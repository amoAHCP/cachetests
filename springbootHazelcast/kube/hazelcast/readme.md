oc apply -f networkpolicy2.yaml // update Namespace name in yaml before
helm install hazelcast hazelcast --values hazelcast.yaml --version 3.3.0 --set securityContext.fsGroup=$( oc get project $(oc project  -q) -ojsonpath='{.metadata.annotations.openshift\.io/sa\.scc\.supplemental-groups}' | cut -f1 -d '/' ) \
--set securityContext.runAsUser=$( oc get project $(oc project  -q) -ojsonpath='{.metadata.annotations.openshift\.io/sa\.scc\.uid-range}' | cut -f1 -d '/' ) 



# apply secret

echo -n "ENT#10Nodes#nPTXQ8qymO1U6BGKgEwHD5AiCY20lbkZMjfNJ9WduS14000110901000131291107000111011900101000001" | base64
add output to secrets.yaml