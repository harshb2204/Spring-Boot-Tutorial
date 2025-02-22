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

