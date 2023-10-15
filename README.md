# Attornatus API - People

Este projeto usa Spring Boot e banco de dados em memória H2
Aplicação com imagem no Docker Hub e Deploy na Amazon em fargate usando pipeline com AWS CDK e CloudFormation  
Desabilite o CORS no seu navegador para testar via swagger ou use o Postman

## Execução do projeto via Docker
Projeto disponível no Docker Hub
* https://hub.docker.com/repository/docker/wellalmeida31/attornatus-api-cad-pessoa/general

```shell script
docker run -p 8080:8080 wellalmeida31/attornatus-api-cad-pessoa:2.2
```
## Pré requisitos para a execução local do projeto

* Maven
* Java 17

## Rodando a aplicação em modo dev

```shell script
mvn spring-boot:run
```

## Empacotando a aplicação

```shell script
./mvnw clean package
```

##  Endpoint OpenAPI Swagger
Local
* http://localhost:8080/swagger-ui/index.html

Produção
* http://servi-alb02-7aqhq2sng17-842354884.us-east-1.elb.amazonaws.com:8080/swagger-ui/index.html

## Download Open API Schema document
Local
* http://localhost:8080/v3/api-docs

Produção
* http://servi-alb02-7aqhq2sng17-842354884.us-east-1.elb.amazonaws.com:8080/v3/api-docs


