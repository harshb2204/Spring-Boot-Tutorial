# Bean Scopes in Spring

In Spring, **bean scope** defines the lifecycle and visibility of a bean within the Spring container. It controls how and when a bean is instantiated and shared across the application. By default, Spring beans follow the **singleton** scope, meaning a single instance is created and managed throughout the application context. However, Spring provides different scopes to suit various use cases.

## Types of Bean Scopes

Spring supports several built-in bean scopes, which can be categorized into **singleton vs. non-singleton scopes**:

### 1. Singleton (Default Scope)

- Only **one** instance of the bean is created per **IoC container**.
- The instance is **eagerly initialized** by the IoC container, meaning it gets created at the time of application startup.
- The same instance is shared across the entire application.
- Best suited for **stateless** and **shared** components.

#### **Code Snippets for Singleton Scope**

Below are examples of `User`, `TestController`, and `TestController2` classes demonstrating the Singleton scope in action.

##### **TestController.java**
```java
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private User user;

    public TestController() {
        System.out.println("TestController Initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestController object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        System.out.println("TestController API invoked");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
```

##### **TestController2.java**
```java
@RestController
@RequestMapping("/test2")
public class TestController2 {
    @Value("${server.port}")
    private String port;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private User user;

    public TestController2() {
        System.out.println("TestController2 Instance Initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestController2 object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        System.out.println("TestController2 API invoked");
        return new ResponseEntity<>("Success from " + port, HttpStatus.OK);
    }
}
```

##### **User.java**
```java
@Component
@Scope("singleton")
public class User {
    public User() {
        System.out.println("User initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }
}
```

### **Explanation**

1. **Singleton Scope (`@Scope("singleton")`)**
   - The `User` bean is a singleton, meaning Spring will create only **one** instance of `User` and share it across different controllers.
   
2. **Controller Instances**
   - `TestController` and `TestController2` each have their own instances.
   - However, both controllers share the same `User` instance (singleton behavior).
   
3. **Output on Application Startup**
   - Since the `User` bean is a singleton, its initialization message appears only once.
   - Each controller prints its hash code, but both print the same `User` instance hash code, proving the singleton behavior.

![](/images/beanscope1.png)

### 2. Prototype Scope

- Each time a new object is created.
- It is lazily initialized, meaning the object is created only when required.

#### **Student.java**
```java
@Component
@Scope("prototype")
public class Student {
    @Autowired
    User user;

    public Student() {
        System.out.println("Student instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("Student object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
    }
}
```

#### **TestController1.java**
```java
@RestController
@Scope("request")
@RequestMapping(value = "/api/")
public class TestController1 {
    @Autowired
    User user;

    @Autowired
    Student student;

    public TestController1() {
        System.out.println("TestController1 instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestController1 object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
        System.out.println("Student object hashCode: " + student.hashCode());
    }

    @GetMapping(path = "/fetchUser")
    public ResponseEntity<String> getUserDetails() {
        System.out.println("TestController1 api invoked");
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
```

#### **User.java**
```java
@Component
@Scope("request")
public class User {
    public User() {
        System.out.println("User initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }
}
```

![](/images/beanscope2.png)

After hitting the endpoint:
![](/images/beanscopes3.png)

### 3. Request Scope

- A new object is created for each HTTP request.
- Lazily initialized.

#### **Student.java**
```java
@Component
@Scope("prototype")
public class Student {
    @Autowired
    User user;

    public Student() {
        System.out.println("Student instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("Student object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
    }
}
```

#### **TestController1.java**
```java
@RestController
@Scope("request")
@RequestMapping(value = "/api/")
public class TestController1 {
    @Autowired
    User user;

    @Autowired
    Student student;

    public TestController1() {
        System.out.println("TestController1 instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestController1 object hashCode: " + this.hashCode());
        System.out.println("User object hashCode: " + user.hashCode());
        System.out.println("Student object hashCode: " + student.hashCode());
    }

    @GetMapping(path = "/fetchUser")
    public ResponseEntity<String> getUserDetails() {
        System.out.println("TestController1 api invoked");
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
```

#### **User.java**
```java
@Component
@Scope("request")
public class User {
    public User() {
        System.out.println("User initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }
}
```

![](/images/beanscopes4.png)

### 4. Important Note on Request Scope

The example below will throw an error:

#### **TestControlleri.java**
```java
@RestController
@Scope("singleton")
@RequestMapping(value = "/api/")
public class TestControlleri {
    @Autowired
    User user;

    public TestControlleri() {
        System.out.println("TestControlleri instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestControlleri object hashCode: " + this.hashCode() +
                           " User object hashCode: " + user.hashCode());
    }

    @GetMapping(path = "/fetchüser") // Corrected annotation
    public ResponseEntity<String> getUserDetails() {
        System.out.println("fetchüser api invoked");
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
```

#### **User.java**
```java
@Component
@Scope("request") // Important: Request scope
public class User {
    public User() {
        System.out.println("User initialization"); // Corrected typo
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }
}
```

![](/images/beanscopes5.png)

### 5. Scoped Proxy Mode

Spring creates request-scoped beans only when an active HTTP request is present. Since a singleton-scoped bean is eagerly initialized, there is no active HTTP request present in the current thread at the time of initialization. This means Spring won't create a bean for User at that moment.

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class User {
    public User() {
        System.out.println("User initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }

    public void dummyMethod() {
    }
}
```

### 6. Session Scope

- New Object is created for each HTTP session.
- Lazily initialized.
- When user accesses any endpoint, session is created.
- Remains active until it does not expire.

#### **TestControlleri.java**
```java
@RestController
@Scope(value = "session")
@RequestMapping(value = "/api/")
public class TestControlleri {
    @Autowired
    User user;

    public TestControlleri() {
        System.out.println("TestControlleri instance initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("TestController1 object hashCode: " + this.hashCode() +
                           " User object hashCode: " + user.hashCode());
    }

    @GetMapping(path = "/fetchUser")
    public ResponseEntity<String> getUserDetails() {
        System.out.println("fetchUser api invoked");
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<String> getUserDetails(HttpServletRequest request) {
        System.out.println("end the session");
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
```

#### **User.java**
```java
@Component
public class User {
    public User() {
        System.out.println("User initialization");
    }

    @PostConstruct
    public void init() {
        System.out.println("User object hashCode: " + this.hashCode());
    }

    public void dummyMethod() {
    }
}
```