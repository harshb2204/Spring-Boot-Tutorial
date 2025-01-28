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


