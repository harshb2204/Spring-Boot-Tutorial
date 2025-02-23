# AOP (Aspect Oriented Programming)

- In simple term, it helps to intercept the method invocation. And we can perform some task before and after the method.

- AOP allows us to focus on business logic by handling boilerplate and repetitive code like logging, transaction management, etc.

- So, Aspect is a module which handles this repetitive or boilerplate code.

- Helps in achieving reusability and maintainability of the code.

## Used during:
- Logging
- Transaction Management
- Security, etc.

## Dependency you need to add in pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
![](/images/aop.png)

##  Pointcut
It's an expression , which tells wherw an advice should be applied.

## Types of Pointcut:

1. **Execution**: matches a particular method in a particular class.
   ```java
   @Before("execution(public String com.conceptandcoding.learningspringboot.Employee.fetchEmployee())")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

   - **(*) wildcard**: matches any single item

   - **Matches any return type**:
   ```java
   @Before("execution(* com.conceptandcoding.learningspringboot.Employee.fetchEmployee())")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

   - **Matches any method with single parameter String**:
   ```java
   @Before("execution(* com.conceptandcoding.learningspringboot.Employee.*(String))")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

   - **Matches fetchEmployee method that takes any single parameter**:
   ```java
   @Before("execution(String com.conceptandcoding.learningspringboot.Employee.fetchEmployee(*))")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```


- **(..) wildcard**: matches 0 or more items.
   Matches `fetchEmployee` method that takes any 0 or more parameters:
   ```java
   @Before("execution(String com.conceptandcoding.learningspringboot.Employee.fetchEmployee(..))")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

-  Matches `fetchEmployee` method in `com.conceptandcoding` package and subpackage classes:
   ```java
   @Before("execution(String com.conceptandcoding..fetchEmployee())")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

- Matches any method in `com.conceptandcoding` package and subpackage classes:
   ```java
   @Before("execution(String com.conceptandcoding..*)")
   public void beforeMethod(){
       System.out.println("inside beforeMethod Aspect");
   }
   ```

## Within
Matches all methods within any class or package.

This pointcut will run for each method in the class Employee:
```java
@Before("within(com.conceptandcoding.learningspringboot.Employee)")
```

This pointcut will run for each method in this package and subpackage:
```java
@Before("within(com.conceptandcoding.learningspringboot..*)")
```

## @within
Matches any method in a class which has this annotation. Here in example service annotation is being referred.

```java
@RestController
@RequestMapping(value = "/api/")
public class Employee {
    @Autowired
    EmployeeUtil employeeUtil;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        employeeUtil.employeeHelperMethod();
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("@within(org.springframework.stereotype.Service)")
    public void beforeMethod() {
        System.out.println("inside beforeMethod aspect");
    }
}
```

```java
@Service
public class EmployeeUtil {
    public void employeeHelperMethod() {
        System.out.println("employee helper method called");
    }
}
```


