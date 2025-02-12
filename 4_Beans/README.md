# Spring Beans

## What is a Bean?

In simple terms, a Bean is a Java Object that is managed by the Spring container (also known as IoC Container - Inversion of Control Container).

The IoC container is responsible for:
- Instantiating beans
- Configuring beans
- Managing bean lifecycle
- Injecting dependencies

## How to Create a Bean?

There are two main ways to create a Bean in Spring:

### 1. Using @Component Annotation

- The `@Component` annotation follows the "convention over configuration" approach.
- This means Spring Boot will try to auto-configure based on conventions, reducing the need for explicit configuration.
- `@Controller`, `@Service`, etc., all internally tell Spring to create a bean and manage it.

So here, Spring Boot will internally call `new User()` to create an instance of this class.

```java
@Component
public class User {

    String username;
    String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```
### Application Startup Failure: Constructor Parameters

**Consider the example below, the application fails to start**

```java
@Component
public class User {

    String username;
    String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
```

Why This Happens:
Spring's dependency injection mechanism needs to know how to create the beans it manages. With a constructor that has arguments, Spring must know where those argument values come from.

To fix this, you have several options:

1. Use `@Value` annotation to provide values:
```java
@Component
public class User {
    String username;
    String email;

    public User(
        @Value("${user.username:defaultUser}") String username,
        @Value("${user.email:default@email.com}") String email
    ) {
        this.username = username;
        this.email = email;
    }
    // ... getters and setters ...
}
```

2. Create a configuration class with `@Bean` method:
```java
@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User("configuredUser", "configured@email.com");
    }
}
```

3. Make the constructor no-args by providing default values:
```java
@Component
public class User {
    String username = "defaultUser";
    String email = "default@email.com";

    public User() {
        // No-args constructor works fine with Spring
    }
    // ... getters and setters ...
}
```

The first approach using `@Value` is particularly useful as it allows you to externalize these values in your application.properties/yaml file:
```properties
user.username=myUser
user.email=my@email.com
```

### Multiple Bean Definitions

When defining beans through configuration, you can create multiple instances of the same type. Each bean will be managed separately by Spring:

```java
@Configuration
public class AppConfig {
    @Bean
    public User createUserBean() {
        return new User("defaultUsername", "defaultemail");
    }

    @Bean
    public User createAnotherUserBean() {
        return new User("anotherUsername", "anotheremail");
    }
}
```

## How Spring Boot Finds Beans

Spring Boot uses component scanning to automatically discover and register beans in your application. Here's how it works:

1. The `@SpringBootApplication` annotation includes `@ComponentScan` by default
2. `@ComponentScan` searches for Spring components in the application's package and sub-packages
3. It looks for classes annotated with:
   - `@Component`
   - `@Service`
   - `@Repository`
   - `@Controller`
   - And other Spring stereotypes

Here's an example of the main application class:

```java
@SpringBootApplication
@ComponentScan(basePackages = "com.conceptandcoding.learningspringboot")
public class SpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
```

Note:
- The `basePackages` parameter is optional - by default, Spring Boot scans from the package containing the main class
- You can specify multiple base packages to scan
- Component scanning helps implement the "convention over configuration" principle in Spring Boot

## Explicit Bean Definition

2. Through explicit defining of a bean via `@Bean` annotation in a `@Configuration` class:

```java
@Configuration
public class AppConfig {
    @Bean
    public User createUserBean() {
        return new User(username: "defaultusername", email: "defaultemail");
    }
}
```

This approach allows you to define beans explicitly, giving you control over their instantiation and configuration. Each method annotated with `@Bean` will return an instance of the specified type, which Spring will manage.

## At What Time Do These Beans Get Created

### Eagerness
- Some beans get created when we start up an application.
- For example: Beans with Singleton Scope are eagerly initialized.

### Lazy
- Some beans get created lazily, meaning they are initialized when actually needed.
- For example: Beans with Scope like Prototype are lazily initialized.
- Or beans annotated with `@Lazy`.

```java
@Lazy
@Component
public class Order {
    public Order() {
        System.out.println("initializing Order");
    }
}
```
### Bean Lifecycle
![beanlifecycle](/images/beanlifecycle.png)

## Spring Bean Lifecycle

1️⃣ **Application Start**  
The Spring application starts, and the Spring Context (IOC container) begins initialization.  
The container reads configuration files (application.properties, application.yml, or Java-based configurations).

2️⃣ **IOC Container Started**  
The IOC (Inversion of Control) container is created and initialized.  
The container scans for beans, components, services, and configurations.  
Configuration files and annotations (@ComponentScan, @Configuration, etc.) are processed.

3️⃣ **Construct Bean**  
Spring creates an instance of the bean using reflection.  
If a bean is defined with @Component, @Service, @Repository, or explicitly via @Bean in a configuration class, the container instantiates the bean.  
This is done using the default constructor or a parameterized constructor with @Autowired dependencies.

4️⃣ **Inject Dependency Into Constructed Bean**  
After creating the bean, Spring injects its required dependencies.  
This can be done via:
- Constructor Injection (@Autowired on a constructor).
- Setter Injection (Spring calls setter methods).
- Field Injection (@Autowired on a field).

5️⃣ **@PostConstruct (Initialization Hook)**  
If the bean has a method annotated with @PostConstruct, this method is executed after dependency injection is completed.  
This method is commonly used for:
- Initializing resources.
- Running setup logic.
- Performing sanity checks.

6️⃣ **Use the Bean**  
The bean is now fully initialized and ready to be used in the application.  
It can be accessed via dependency injection in controllers, services, or other components.

7️⃣ **@PreDestroy (Cleanup Hook)**  
If a bean has a method annotated with @PreDestroy, it runs before the bean is destroyed.  
This is useful for:
- Closing database connections.
- Releasing resources (files, sockets, threads).
- Logging cleanup actions.

8️⃣ **Bean Destroyed**  
The Spring container removes the bean from memory when it's no longer needed.  
This happens during:
- Application shutdown (context.close()).
- Scope-based lifecycle completion (e.g., request-scoped beans in a web app).



