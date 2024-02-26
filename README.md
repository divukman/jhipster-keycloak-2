# JHipster + Keycloak example

## Jhipster (Spring Boot and Vue) with Keycloak auth server.
* keycloak with realm jhipster, users juser/user and jadmin/admin

## Run
### Keycloak (with h2)
```
$ cd docker-keycloak
$ ./startMeUp.sh
```

### Spring Boot server
```
$ cd hello_world_keycloak
$ ./mvnw spring-boot:run
```

### Vue
```
$ cd hello_world_keycloak
$ npm install
$ npm start
```
 
