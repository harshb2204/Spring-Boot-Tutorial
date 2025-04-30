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

## API Gateway

APIs are a common way of communication between applications. In the case of microservice architecture, there will be a number of services and the client has to know the hostnames of all underlying applications to invoke them.

To simplify this communication, we prefer a component between client and server to manage all API requests called API Gateway. Additionally, we can have other features which include:

* **Security** - Authentication, authorization
* **Routing** - routing, request/response manipulation, circuit breaker
* **Observability** - metric aggregation, logging, tracing
![](/images/apigateway.png)

## Spring Cloud API Gateway

Spring Cloud API Gateway is a powerful, flexible solution for routing and proxying requests to downstream services in a microservices architecture. It handles several important tasks like routing, filtering, authentication, and load balancing.
![](/images/springcloudapigateway.png)

- create a spring project with gateway, eureka client dependencies

## Spring Cloud Gateway Building Blocks

Spring Cloud Gateway consists of 3 main building blocks:

1. **Route**: The basic building block of the gateway. It consists of:
   - ID: Unique identifier for the route
   - URI: The destination URI where the request will be sent
   - Predicates: Conditions that must be met to match the route
   - Filters: Modify requests and responses before or after sending the downstream request

2. **Predicate**: Conditions that must be true for the route to be matched. Predicates can be based on:
   - Path
   - Method
   - Header
   - Query Parameter
   - Cookie
   - Host
   - Time

3. **Filters**: Allow modification of requests and responses. They can:
   - Add/Remove Headers
   - Add/Remove Parameters
   - Rewrite Paths
   - Set Status
   - Add Authentication
   - Rate Limiting
   - Circuit Breaking

Example Configuration:
```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: order-service
        uri: lb://ORDER-SERVICE
        predicates:
        - Path=/api/v1/orders/**
        filters:
        - AddRequestHeader=X-Custom-Header, CustomValue
      - id: inventory-service
        uri: lb://INVENTORY-SERVICE
        predicates:
        - Path=/api/v1/inventory/**
```

## Spring Cloud Gateway Components in Detail

### 1. Route
Think of this as the destination that we want a particular request to route to. It comprises of destination URI, a condition that has to satisfy - or in terms of technical terms, Predicates, and one or more filters.

### 2. Predicate
This is literally a condition to match, i.e. kind of "if" condition. If requests has something - e.g. path=blah or request header contains foo-bar etc.

Examples of Predicates:
```yaml
# Predicates with path
- Path=/api/v1/orders/**

# Predicates with Method
- Method=GET

# Predicates with Header
- Header=User-Agent, Mozilla/*
```

### 3. Filter
These are instances of Spring Framework WebFilter. This is where you can apply your magic of modifying request or response. There are quite a lot of out of box WebFilter that framework provides.

Example Filters:
```yaml
filters:
  # Add a request header
  - AddRequestHeader=X-Request-Id, 12345
  
  # Add a response header
  - AddResponseHeader=X-Response-id, abcd
  
  # Redirect to another URL
  - RedirectTo=302, https://youtube.com
  
  # Strip prefix from path
  - StripPrefix=1
  
  # Remove a request header
  - RemoveRequestHeader=Cookie
```

## Spring Cloud Open Feign

Spring Cloud OpenFeign is a declarative HTTP client library for building RESTful microservices. It integrates seamlessly with Spring Cloud and simplifies the development of HTTP clients by allowing you to create interfaces that resemble the API of the target service. It abstracts away much of the boilerplate code typically associated with making HTTP requests, making your codebase cleaner and more maintainable.

### Adding OpenFeign Dependency

To use Spring Cloud OpenFeign, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### Using OpenFeign

Here's an example of how to use OpenFeign in your microservices:

1. First, create a Feign client interface:

```java
@FeignClient(name = "order-service", path = "/orders")
public interface OrdersFeinClient {
    @GetMapping("/core/helloOrders")
    String helloOrders();
}
```

2. Use the Feign client in your controller:

```java
@RestController
public class InventoryController {
    private final RestClient restClient;
    private final OrdersFeinClient ordersFeinClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(HttpServletRequest httpServletRequest) {
        log.info(httpServletRequest.getHeader("x-custom-header"));
        
        // Using Feign client instead of manual RestClient
        return ordersFeinClient.helloOrders();
        
        /* Alternative approach using RestClient:
        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();
        String response = restClient.get()
            .uri(orderService.getUri()+ "/orders/core/helloOrders")
            .retrieve()
            .body(String.class);
        return response;
        */
    }
}
```

The example above demonstrates how OpenFeign simplifies HTTP client code by:
- Declaring the API endpoint using annotations
- Automatically handling service discovery through the service name
- Eliminating boilerplate code for making HTTP requests
- Providing a clean, interface-based approach to defining API clients

## Eureka Instance Configuration

When services register with Eureka, they may face issues with hostname resolution, especially in Windows or Docker environments. By default, Eureka uses the machine's hostname (e.g., LAPTOP-XXXX.mshome.net) for registration, which can cause problems with service discovery.

To ensure proper service discovery and communication between services (especially for API Gateway and OpenFeign clients), add the following configuration to your service's `application.yml` or `application.properties`:

```yaml
eureka:
  instance:
    prefer-ip-address: true
```

Or in `.properties` format:
```properties
eureka.instance.prefer-ip-address=true
```

This configuration tells Eureka to register services using their IP addresses instead of hostnames, which helps resolve common service discovery issues and ensures that Feign clients and load balancers work correctly.

