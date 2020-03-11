Apache Shiro + Spring Web Example
=================================

A Spring Boot example web application that show the usage of a user login, checking permissions, and annotation protected methods.

Run the Example
---------------

```
mvn spring-boot:run
```

Browse to `http://localhost:8080/`

Or deploy to Heroku:

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/bdemers/heroku-examples-runner&env\[ARTIFACT_ID\]=samples-spring-boot-web&&env\[RUNNER\]=spring-boot)


## 简介 

### 功能：

* **Authentication** - proving user identity, often called user ‘login’.

* **Authorization** - access control

* **Cryptography** - protecting or hiding data from prying eyes

* **Session Management** - per-user time-sensitive state


### 组件模型

* **Subject** - Security specific user ‘view’ of an application user. It can be a human being, a third-party process, a server connecting to you application, or even a cron job. Basically, it is anything or anyone communicating with your application.

* **Principals** - A subjects identifying attributes. First name, last name, social security number, username

* **Credentials** - secret data that are used to verify identities. Passwords, Biometric data, x509 certificates,

* **Realms** - Security specific DAO, data access object, software component that talks to a backend data source. If you have usernames and password in LDAP, then you would have an LDAP Realm that would communicate with LDAP. The idea is that you would use a realm per back-end data source and Shiro would know how to coordinate with these realms together to do what you have to do.

* **Permission** - Shiro defines a Permission as a statement that defines an explicit behavior or action. It is a statement of raw functionality in an application and nothing more. Permissions are the lowest-level constructs in security polices, and they explicitly define only “what” the application can do. They do not at all describe “who” is able to perform the action(s).
Shiro provides powerful and intuitive permission syntax we refer to as the WildcardPermission. Wildcard Permissions supports multiple levels of permissioning.


