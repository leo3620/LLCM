Pour run le back :

mvn clean package
docker build -t llcm .
docker run -it -p 8080:8080 llcm
