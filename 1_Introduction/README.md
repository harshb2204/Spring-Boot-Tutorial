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


