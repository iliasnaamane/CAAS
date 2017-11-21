#!/bin/bash
#deployment
cd all
mvn appengine:update
cd ..
../Downloads/appengine-java-sdk-1.9.59/bin/appcfg.sh update_dispatch services/target/services-1.0 -A sacc-belhassen-182811 -V 1 
