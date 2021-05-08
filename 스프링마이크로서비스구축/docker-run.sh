## docker 이미지 빌드
cd microservice/product-service
docker build -t product-service .

## docker 시작
docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name my-prd-srv product-service

## docker 중지 및 제거
docker rm -f my-prd-srv