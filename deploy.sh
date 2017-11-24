#!/bin/bash
#deployment
#/home/user/.m2/repository/com/google/appengine/appengine-java-sdk/1.9.42/appengine-java-sdk/appengine-java-sdk-1.9.42/bin/appcfg.sh update_dispatch services/target/services-1.0 -A sacc-belhassen-182811 -V 1
cd all
mvn appengine:update
#cd ..
#../Downloads/appengine-java-sdk-1.9.59/bin/appcfg.sh update_dispatch services/target/services-1.0 -A sacc-belhassen-182811 -V 1

#/home/user/Bureau/appengine-java-sdk-1.9.59/bin/appcfg.sh update_dispatch services/target/services-1.0 -A sacc-belhassen-182811 -V 1
