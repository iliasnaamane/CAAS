## Readme
This is a REST based service. It uses the Rest protocol to achieve its exchanges.

## How to test
To launch and test the application in local : 


   ../CAAS $   mvn clean install
   
   
   ../CAAS/all $ mvn appengine:devserver
   
to consult the entities :  http://localhost:8080/_ah/admin/

## EndPoint
### create user
Post http://localhost:8080/userCreate

request
```xml
{
"username" :"issam",
"mail" : "issambelhassen@yahoo.fr",
"offer" : 1
}
```
### Upload vido
http://localhost:8080/uploadVideo

request
```xml
{"username" :"issam",
"videoName" : "video1",
"format" : "mp4",
"videoDuration" : 12.1
}
``
