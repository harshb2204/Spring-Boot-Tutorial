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

## @annotation
Matches any method that is annotated with a given annotation.

```java
@RestController
@RequestMapping(value = "/api/")
public class Employee {
    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void beforeMethod() {
        System.out.println("inside beforeMethod Aspect");
    }
}
```

## Args
Matches any method with particular arguments (or parameters).
```java
@Before("args(String, int)")
public void beforeMethod() {
    System.out.println("inside beforeMethod aspect");
}
```

### Example
```java
@RestController
@RequestMapping(value = "/api/")
public class Employee {
    @Autowired
    EmployeeUtil employeeUtil;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        employeeUtil.employeeHelperMethod("xyz", 123);
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("args(String, int)")
    public void beforeMethod() {
        System.out.println("inside beforeMethod aspect");
    }
}
```

```java
@Service
public class EmployeeUtil {
    public void employeeHelperMethod(String str, int val) {
        System.out.println("employee helper method called");
    }
}
```

## target
Matches any method on a particular instance of a class.
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
    @Before("target(com.conceptandcoding.learningspringboot.EmployeeUtil)")
    public void beforeMethod() {
        System.out.println("inside beforeMethod aspect");
    }
}
```

```java
@Component
public class EmployeeUtil {
    public void employeeHelperMethod() {
        System.out.println("employee helper method called");
    }
}
```

## target with Interface
Matches any method on a particular instance of a class that implements a specified interface.

In this example, the `LoggingAspect` class uses the `@Before` annotation to intercept method calls on any class that implements the `IEmployee` interface. This allows for centralized logging or other cross-cutting concerns.

```java
@RestController
@RequestMapping(value = "/api/")
public class EmployeeController {
    @Autowired
    @Qualifier("tempEmpEmployee")
    IEmployee employeeObj;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        employeeObj.fetchEmployeeMethod();
        return "item fetched";
    }
}
```

```java
public interface IEmployee {
    void fetchEmployeeMethod();
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("target(com.conceptandcoding.learningspringboot.IEmployee)")
    public void beforeMethod() {
        System.out.println("inside beforeMethod aspect");
    }
}
```

```java
@Component
@Qualifier("tempEmployee")
public class TempEmployee implements IEmployee {
    @Override
    public void fetchEmployeeMethod() {
        System.out.println("in temp Employee fetch method");
    }
}
```

```java
@Component
@Qualifier("permaEmployee")
public class PermanentEmployee implements IEmployee {
    @Override
    public void fetchEmployeeMethod() {
        System.out.println("inside permanent fetch employee method");
    }
}
```

## Combining Pointcuts
You can combine two pointcuts using boolean operators:

- `&&` (boolean AND): Both conditions must be true.
- `||` (boolean OR): At least one condition must be true.

### Example
In this example, the `LoggingAspect` class uses combined pointcuts to intercept method calls in the `EmployeeController` class based on specific conditions.

```java
@RestController
@RequestMapping(value = "/api/")
public class EmployeeController {
    @Autowired
    EmployeeUtil employeeUtil;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.conceptandcoding.learningspringboot.EmployeeController.*()) " +
            "&& @within(org.springframework.web.bind.annotation.RestController)")
    public void beforeAndMethod() {
        System.out.println("inside beforeAndMethod aspect");
    }

    @Before("execution(* com.conceptandcoding.learningspringboot.EmployeeController.*()) " +
            "|| @within(org.springframework.stereotype.Component)")
    public void beforeOrMethod() {
        System.out.println("inside beforeOrMethod aspect");
    }
}
```

## Named Pointcuts
Named pointcuts allow you to define a pointcut expression once and reuse it in multiple advice methods. This enhances code readability and maintainability.

### Example
In this example, the `LoggingAspect` class defines a named pointcut `customPointcutName` that matches all methods in the `EmployeeController` class. The `beforeMethod` advice uses this named pointcut.

```java
@RestController
@RequestMapping(value = "/api/")
public class EmployeeController {
    @Autowired
    EmployeeUtil employeeUtil;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.conceptandcoding.learningspringboot.EmployeeController.*())")
    public void customPointcutName() {
        // always stays empty
    }

    @Before("customPointcutName()")
    public void beforeMethod() {
        System.out.println("inside beforeMethod aspect");
    }
}
```

## Advice
Advice is an action taken either @Before, @After, or @Around the method execution. It allows you to define what should happen at specific points in the execution of your program.

### Types of Advice
- **@Before**: Executes before the method execution.
- **@After**: Executes after the method execution.
- **@Around**: Wraps the method execution, allowing you to perform actions both before and after the method call.

## @Around
As the name suggests, `@Around` advice surrounds the method execution, allowing you to perform actions both before and after the method call.

### Example
In this example, the `LoggingAspect` class uses `@Around` advice to log messages before and after the execution of the `employeeHelperMethod`.

```java
@RestController
@RequestMapping(value = "/api/")
public class EmployeeController {
    @Autowired
    EmployeeUtil employeeUtil;

    @GetMapping(path = "/fetchEmployee")
    public String fetchEmployee() {
        return "item fetched";
    }
}
```

```java
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.conceptandcoding.learningspringboot.EmployeeUtil.*())")
    public void aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("inside before Method aspect");
        joinPoint.proceed();
        System.out.println("inside after Method aspect");
    }
}
```

```java
@Component
public class EmployeeUtil {
    public void employeeHelperMethod() {
        System.out.println("employee helper method called");
    }
}
```
## JoinPoint 
Generally considered as a point where actual method invoation happens.
