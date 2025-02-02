# Introduction to Spring Boot
First we need to understand about Servlet or Servlet Container.
-provide foundation for building web applications.
-it is java class that handles client requests, processes it and return the response.
-servlet containers are the ones which manage the servlets.
# Java Servlet Setup Before Spring Boot
## Overview
In traditional Java web applications, servlets were used to handle HTTP requests and generate responses. The configuration of servlets was done manually through the `web.xml` file, which acted as the deployment descriptor for the web application.

## Key Concepts

### 1. **Servlets**
- Servlets are Java classes that extend `HttpServlet` and override methods like `doGet()` and `doPost()` to handle HTTP requests.
- Servlets process requests, interact with business logic, and return HTTP responses.

### 2. **web.xml Configuration**
- Servlets were configured in the `web.xml` file located in the `WEB-INF` directory.
- This file defines servlet names, their corresponding classes, and URL patterns to map incoming requests.

Example of `web.xml` configuration:
   ```xml
   <servlet>
       <servlet-name>MyServlet</servlet-name>
       <servlet-class>com.example.MyServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>MyServlet</servlet-name>
       <url-pattern>/myServlet</url-pattern>
   </servlet-mapping>
   ```
# Project Flow: Servlets 

1. **Project Setup**: Create a Java web project with `web.xml` and servlet classes.

2. **Create Servlet**: Write servlet classes that extend `HttpServlet` and handle HTTP requests (`doGet()`/`doPost()`).

3. **Configure in `web.xml`**: Define servlet mappings to URLs in `web.xml`.

4. **Deploy to Tomcat**: Package as a WAR file and deploy it to the Tomcat `webapps` directory.

5. **Tomcat Routes Requests**: Tomcat matches incoming requests to the appropriate servlet based on URL mapping.

6. **Servlet Processes Request**: Servlet handles the request and prepares the response.

7. **Send Response**: Servlet sends the response back to the client via Tomcat.

8. **Shutdown**: Stop Tomcat when done.

This is the basic flow of using servlets with Tomcat.

## How Spring solves issues of Servlets
### 1. **Removal of web.xml**
- the web.xml becomes very big overtime and is hard to manage.
- spring framework introduces annotations based configuration.

### 2. **Inversion of control**
- IoC is more flexible way to manage the object dependencies and its lifecycle(through dependency injection).


## Dependency Injection 
- Below is a piece of code without the use of dependency injection
 ```java
    public class User{
      public void getUserDetails(String id){
        //business logic here
      }  
    }
    

    public class Payment{
     User sender = new User();
     
     void sendUserDetails(){
        sender.getUserDetails(1);
     }
    }
```
## Tight Coupling Issue in Payment Class

The `Payment` class is currently creating an instance of the `User` class. This design introduces a major problem: **tight coupling**.

### What is Tight Coupling?

Tight coupling means that the `Payment` class is directly dependent on the `User` class.  Changes to the `User` class can directly impact the `Payment` class, and vice versa. This lack of isolation makes the code less flexible, maintainable, and testable.

### Problems Caused by Tight Coupling

Here's how tight coupling manifests as problems in this scenario:

* **Difficult Unit Testing:**
    -  When writing unit tests for the `Payment` class's `getSenderDetails()` method, we need to isolate it from the `User` class's actual behavior.
    -  However, because `Payment` creates a new `User` object internally, we cannot easily mock or stub the `User` object.
    -  This means the tests will inadvertently invoke real methods of the `User` class, making the tests brittle and potentially dependent on external factors (like a database or API if the `User` class interacts with one).

* **Limited Flexibility and Extensibility:**
    -  If, in the future, we need to support different types of users (e.g., "admin", "Member"), the current implementation makes it difficult to switch between user types dynamically.
    -  The `Payment` class is hardcoded to create a specific type of `User` object, requiring modification to the `Payment` class itself whenever we need to handle a new user type.


### Example with Spring Annotations

```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class Payment {
@Autowired
User sender;
void getSenderDetails(String userID) {
sender.getUserDetails(userID);
}
}
@Component
public class User {
public void getUserDetails(String id) {
// do something
}
}
```

# Spring Framework Integration

An important feature of the Spring framework is the extensive **INTEGRATION** available with other frameworks.

This allows developers to choose different combinations of technologies and frameworks that best fit their requirements, such as:

- Integration with unit testing frameworks like JUnit or Mockito.
- Integration with data access frameworks like Hibernate, JDBC, JPA, etc.
- Integration with asynchronous programming.

In a similar way, it has different integrations available for:

- Caching
- Messaging
- Security, etc.
# Spring MVC Architecture

This document explains the architecture of a Spring MVC application as depicted in the provided image.  The diagram illustrates the flow of a request through the various components of the Spring framework.

![Spring MVC Architecture Diagram](/images/springmvc.png)  ## Components

1. **Client Request:** The process begins with a client (e.g., a web browser) sending an HTTP request to the server.  The example URL `/ene end!` suggests a specific endpoint being targeted.

2. **Servlet Container (Tomcat):** The request arrives at the Servlet Container, in this case, Tomcat. Tomcat is responsible for hosting and managing web applications. The Spring MVC application is deployed within Tomcat.

3. **DispatcherServlet:** The `DispatcherServlet` is the front controller in Spring MVC. It handles all incoming requests.  Upon receiving a request, it performs the following steps:

    - **HandlerMapping:** The `DispatcherServlet` consults one or more `HandlerMappings` to determine the appropriate controller to handle the request. `HandlerMappings` maintain the mappings between incoming request URLs and controller classes.

    - **Controller Creation (IoC Container):** Once the controller is identified, the `DispatcherServlet` uses the IoC (Inversion of Control) container (Spring's core container) to create an instance of the controller. This includes injecting any dependencies the controller might have.

    - **Controller Invocation:** The `DispatcherServlet` invokes the appropriate handler method on the selected controller instance.  This is where the core logic of the application resides. The controller processes the request, interacts with models (data), and performs actions.  The diagram notes "Respective API get invoked," indicating that the controller method might call other services or APIs.

    - **Response Generation:** After the controller finishes its task, it returns a logical view name or directly returns a model and view. The `DispatcherServlet` then works with `ViewResolvers` (not shown in the diagram) to select the appropriate view technology (e.g., JSP, Thymeleaf, etc.) and render the response.  Finally, the response is sent back to the client through Tomcat.

## Request Flow Summary

1. Client sends an HTTP request.
2. Tomcat receives the request.
3. The `DispatcherServlet` intercepts the request.
4. The `DispatcherServlet` uses `HandlerMapping` to find the appropriate controller.
5. The `DispatcherServlet` uses the IoC container to create a controller instance (with dependency injection).
6. The `DispatcherServlet` invokes the controller's handler method.
7. The controller processes the request and may interact with other services.
8. The controller returns a view name or model and view.
9. The `DispatcherServlet` uses `ViewResolvers` to select and render the view.
10. Tomcat sends the response back to the client.

## Key Principles

- **Centralized Handling:** The `DispatcherServlet` handles all requests, providing a central point for request processing and control flow.
- **Configuration over Coding:** Spring's `HandlerMapping` and IoC container promote configuration over coding, making it easier to manage and maintain the application.
- **Loose Coupling:** The architecture encourages loose coupling between components. For example, the controller doesn't need to know how the view is rendered.

## Diagram Notes

- "SMVC" likely stands for Spring MVC.
- The "Sync Error" message in the image is unrelated to the architecture diagram itself.
- The handwritten annotations suggest a whiteboard diagram or quick sketch.

This document provides a simplified overview of the Spring MVC architecture depicted in the image. Real-world applications may involve more complex configurations and components.