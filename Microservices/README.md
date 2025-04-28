# Microservices

- Microservices are an architectural style where applications are
developed as a collection of small, loosely coupled, independently
deployable services.
![](/images/microservices.png)

## Characteristics of Microservices
- Small, focused on doing one thing well
- Independently deployable
- Organized around business capabilities
- Decentralized data management
- Communication via lightweight protocols (typically HTTP/REST)

## Comparison with Monolith
- Monolith: tightly coupled, single codebase
- Microservices: loosely coupled, multiple independent services

## Challenges of Microservices
- Increased complexity in managing multiple services
- Distributed System Issues like Latency, load balancing, network reliability, and consistency
- Managing transactions and consistency across services
- Handling communication protocols (REST, gRPC, messaging)
- Monitoring and Logging needs centralized monitoring and logging solutions

## Why use Microservices
- Scale services independently based on demand (e.g., scale only payment service during high traffic)
- Services can be written in different programming languages, suited to specific tasks
- Small teams can work on different services simultaneously, reducing time to market
- Failure in one service doesn't bring down the entire system
- Each service can be updated, deployed, and scaled independently
- Teams can work on different services without affecting each other

## Service Registry
- It is a central location all microservices will be registered here, and they can connect with the service registery to get the address of other service
![](/images/serviceregistery.png)

## What is Service Discovery
- In a microservices architecture, each microservice is a standalone application with
specific business functionality. Since these microservices need to communicate with
each other to function as a complete application, they need to know each other's
network locations. Service Discovery comes into play here, maintaining a record of
these services' locations, helping them find each other, and enabling
communication.

## Spring Cloud Eureka(developed by netflix)
- Eureka is a REST based service which is
primarily used for acquiring information about
services that you would want to communicate
with. This REST service is also known as
Eureka Server. The Services that register in
Eureka Server to obtain information about each
other are called Eureka Clients.
![](/images/eureka.png)



## How Eureka Works

### Service Registration
A service registers itself with the Eureka Server upon startup.

### Heartbeat
The service sends heartbeats periodically to renew its lease with the Eureka Server.

### Service Discovery
Other services can query Eureka to discover the location (IP and port) of the registered service.

### Health Check
Eureka performs health checks to ensure that registered services are still healthy.

### Eviction
If a service stops sending heartbeats and its lease expires, the Eureka Server evicts it from the registry.

## Configuring the eureka server
- Add these config to your discovery service
```properties 
# do not register as client for eureka server
eureka.client.register-with-eureka=false
#do not fetch registry
eureka.client.fetch-registry=false
``` 
- Add necessary dependencies in the pom as listed
- eureka server dependency for the discovery service
- eureka client dependency for other services
- add spring cloud dependency management and its version in the pom
- Add these config to your other services
```properties
#let eureka client know where to find eureka server
eureka.client.service.url.defaultZone=http://localhost:8761/eureka/
```
