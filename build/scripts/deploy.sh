cd /home/ec2-user/bierkasse
docker image build -t docker-java-jar:latest .
docker run -p 8080:8080 docker-java-jar:latest