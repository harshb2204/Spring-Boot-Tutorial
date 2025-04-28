# Interceptor
Its a mediator, which get invoked before or after your actual code.
Custom interceptor for requests before even reaching to specific controller class.
![](/images/interceptor.png)

aftercompletion runs even if an exception occurs.

Custom interceptor for requests after reaching to specific controller class.

## Custom Annotations
### Step 1: Creation of custom annotation

We can create Custom Annotation using keyword `@interface` Java Annotation.

```java
public @interface MyCustomAnnotation {
}
```

Example usage:
```java
public class User {
    @MyCustomAnnotation
    public void updateUser(){
        //some business logic
    }
}
```

Important Meta Annotation properties:
- **@Target**: Specifies where the annotation can be applied (methods, classes, constructors, etc.)

Example of @Target usage:
```java
@Target(ElementType.METHOD)
public @interface MyCustomAnnotation {
}

// Multiple targets
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface MyCustomAnnotation {
}
```

- **@Retention**: Specifies how the annotation will be stored in Java.
  - `RetentionPolicy.SOURCE`: Annotation will be discarded by compiler itself and its not even recorded in .class file.
  - `RetentionPolicy.CLASS`: Annotation will be recorded in .class file but ignored by JVM during run time.
  - `RetentionPolicy.RUNTIME`: Annotation will be recorded in .class file and also available during run time.

![](/images/interceptors2.png)

### Creating Custom Annotations with Methods

Custom annotations can have methods (which act like fields). Here are the key rules:
- No parameter, no body
- Return type is restricted to:
  - Primitive types (int, boolean, double etc.)
  - String
  - Enum
  - Class<?>
  - Annotations
  - Array of above types

Example of custom annotation with method:
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomAnnotation {
    String key() default "defaultKeyName";
}

// Usage example
public class User {
    @MyCustomAnnotation(key = "userKey")
    public void updateUser(){
        //some business logic
    }
}
```

### Step 2: Creation of Custom Interceptor

Here's an example of how to create and use a custom interceptor with annotations:

```java
// Controller class
@RestController
@RequestMapping(value = "/api/")
public class UserController {
    @Autowired
    User user;

    @GetMapping(path = "/getUser")
    public String getUser(){
        user.getUser();
        return "success";
    }
}

// Custom Annotation definition
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomAnnotation {
    String name() default "";
}

// Component class using the custom annotation
@Component
public class User {
    @MyCustomAnnotation(name = "user")
    public void getUser() {
        System.out.println("get the user details");
    }
}
```

```java
// Custom Interceptor implementation
@Component
@Aspect
public class MyCustomInterceptor {
    
    @Around("@annotation(com.conceptandcoding.learningspringboot.CustomInterceptor.MyCustomAnnotation)") //pointcut expression
    public void invoke(ProceedingJoinPoint joinPoint) throws Throwable { //advice
        
        System.out.println("do something before actual method");
        
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        if(method.isAnnotationPresent(MyCustomAnnotation.class)){
            MyCustomAnnotation annotation = method.getAnnotation(MyCustomAnnotation.class);
            System.out.println("name from annotation: " + annotation.name());
        }
        
        joinPoint.proceed();
        System.out.println("do something after actual method");
    }
}
![](/images/interceptoroutput.png)

## Springboot: Filters Vs Interceptors

### Filter:
It intercept the HTTP Request and Response, before they reach to the servlet.

### Interceptor:
Its specific to Spring framework, and intercept HTTP Request and Response, before they reach to the Controller.

![](/images/filters.png)

## What is Servlet
A Servlet is nothing but a Java class, which accepts the incoming request, process it and returns the response.

We can create multiple servlets like:
- Servlet 1: can be configured to handle REST APIs
- Servlet 2: can be configured to handle SOAP APIs etc...

Similarly like this, "DispatcherServlet" is kind of servlet provided by spring, and by default its configured to handle all APIs "/*".



### Filter:
Is used when we want to intercept HTTP Request and Response and add logic agnostic of the underlying servlets.
We can have many filters and have ordering between them too.

### Interceptors:
Is used when we want to intercept HTTP request and response and add logic specific to a particular servlet.
We can have many Interceptors and have ordering between them too.
