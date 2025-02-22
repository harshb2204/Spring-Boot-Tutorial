![](/images/environments.png)
Username password is just one example, there are so many configurations
- URL and Port number
- Connection timeout values
- Request timeout values
- Throttle values
- Retry values etc.

Application profiling in software development allows you to use different configurations (properties files) for various environments or scenarios.

application.properties is the default configuration.
application-{profile}.properties files override the default with profile-specific settings.

![](/images/profiling.png)
```java
@Component
public class MySQLConnection {
@Value("${username}")
String username;
@Value("${password}")
String password;
@PostConstruct
public void init() {
System.out.println("username: " + username + ", password: " + password);
}
}
```

**application.properties**
```
username=defaultUsername
password=defaultPassword
```

**application-dev.properties**
```
username=devUsername
password=devPassword
```

**application-qa.properties**
```
username=qaUsername
password=qaPassword
```

**application-prod.properties**
```
username=prodUsername
password=prodPassword
```
Here the default one is picked by springboot, aas we have not set the profile.


And during application startup, we can tell Spring Boot to pick a specific "application properties" file using the `spring.profiles.active` configuration.
**application.properties**
```
username=defaultUsername
password=defaultPassword
spring.profiles.active=dev
```

We can pass the value to this configuration `spring.profiles.active` during application startup itself.

```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

or 

Add this in `Pom.xml`:
```xml
<profiles>
    <profile>
        <id>local</id>
        <properties>
            <spring-boot.run.profiles>dev</spring-boot.run.profiles>
        </properties>
    </profile>
    <profile>
        <id>production</id>
        <properties>
            <spring-boot.run.profiles>prod</spring-boot.run.profiles>
        </properties>
    </profile>
    <profile>
        <id>stage</id>
        <properties>
            <spring-boot.run.profiles>qa</spring-boot.run.profiles>
        </properties>
    </profile>
</profiles>
```

```
mvn spring-boot:run -Pproduction
```

# Spring Boot `@Profile` Annotation

## Overview
The `@Profile` annotation in Spring Boot allows developers to define environment-specific beans and configurations. It ensures that certain components are only loaded when a specific profile is active, making it easier to manage different environments like development, testing, and production.

## How `@Profile` Works
- Beans or configuration classes marked with `@Profile("profileName")` are only loaded if the specified profile is active.
- Multiple profiles can be specified using `@Profile({"profile1", "profile2"})`.
- If no profile is specified, the default configuration is used.

## Setting Active Profile
### 1. In `application.properties`
```properties
spring.profiles.active=dev
```
### 2. Using Command Line Arguments
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
### 3. Using Environment Variables
```sh
export SPRING_PROFILES_ACTIVE=qa
```

## Example Usage
### Conditional Beans with `@Profile`
```java
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDatabaseConfig {
    public DevDatabaseConfig() {
        System.out.println("Development Database Configuration Loaded");
    }
}
```
```java
@Component
@Profile("prod")
public class ProdDatabaseConfig {
    public ProdDatabaseConfig() {
        System.out.println("Production Database Configuration Loaded");
    }
}
```
- When the profile is set to `dev`, only `DevDatabaseConfig` will be loaded.
- When the profile is set to `prod`, only `ProdDatabaseConfig` will be loaded.

### Using `@Profile` in Configuration Classes
Instead of marking individual beans, you can annotate entire configuration classes:
```java
@Configuration
@Profile("qa")
public class QAConfig {
    @Bean
    public String qaDataSource() {
        return "QA Database Connected";
    }
}
```




## Summary
- `@Profile` allows environment-specific configurations.
- You can set profiles in properties files, command-line arguments, or environment variables.
- Profiles can be used with components or entire configuration classes.
- Maven profiles help activate different Spring profiles during builds.

Using `@Profile` helps in managing different configurations efficiently and avoids hardcoding environment settings in the application code.

