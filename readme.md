# Readme
This is a REST based service. It uses the Rest protocol to achieve its exchanges.

#### How to test

##### To deploy and test the application in appEngine : 

../CAAS $ ./build.sh

../CAAS $ ./deploy.sh

##### To launch and test the application in local : 

   ../CAAS $  ./build.sh
    
   ../CAAS/all $ mvn appengine:devserver
   
to consult the entities :  http://localhost:8080/_ah/admin/

## EndPoint
### create user
Posthttps://services-dot-sacc-belhassen-182811.appspot.com/userCreate

request
```
{
"username" :"issam",
"mail" : "issambelhassen@yahoo.fr",
"offer" : 1
}
```

result
```
user has been successfully created 
{
"id":5631986051842048,
"username" :"issam",
"mail" : "issambelhassen@yahoo.fr",
"offer" : 1
}
```
### Create and store video
POST https://services-dot-sacc-belhassen-182811.appspot.com/uploadVideo

request
```
{
"videoName" : "video6",
"videoDuration" : 14
}

```

result
```
video Original was save in cloud storage : 
video6-dfe9078c-fb7c-470f-a2ef-8bd7e379463c

```
### Conversion video
POST https://services-dot-sacc-belhassen-182811.appspot.com/conversionVideo

request
```
{
	"username" : "issam",
	"original" : "video6-dfe9078c-fb7c-470f-a2ef-8bd7e379463c",
  "format" : "mp8"
}
```

result
```
user has been found successfully 
task has been created successfully 
queue change 
```
