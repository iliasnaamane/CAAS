#ab -c 3 -n 1 -p post.txt  -H   -T application/x-www-form-urlencoded http://localhost:8080/conversionVideo  
#ab -p post1.txt -T application/json -H  -c 3 -n 1 http://localhost:8080/conversionVideo & 
#ab -p post2.txt -T application/json -H  -c 3 -n 1 http://localhost:8080/conversionVideo & 


ab -c 5 -n 10 -r  -p   post.json -T application/json  http://127.0.0.1:8080/conversionVideo 
#ab -c 3 -n 10 -r  -p   post1.json -T application/json  http://127.0.0.1:8080/conversionVideo &
#ab -c 3 -n 10 -r  -p   post2.json -T application/json  http://127.0.0.1:8080/conversionVideo &
#ab -c 3 -n 10 -r  -p   post3.json -T application/json  http://127.0.0.1:8080/conversionVideo