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


