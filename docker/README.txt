docker build . -t waqqassharif/nginx
docker images | grep nginx
docker tag <IMAGE_ID> waqqassharif/nginx:latest
docker push waqqassharif/nginx:latest
docker image rm -f IMAGE_ID>
docker pull waqqassharif/nginx:latest
$ docker run -p 80:80 waqqassharif/nginx