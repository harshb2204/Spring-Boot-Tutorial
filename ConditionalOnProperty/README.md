# ConditionalOnProperty

## Overview
The `@ConditionalOnProperty` annotation allows for the conditional creation of beans based on specified properties. This means that a bean can be created or not, depending on the configuration.



```java
@Component
public class DBConnection {
    @Autowired
    MySQLConnection mySQLConnection;

    @Autowired
    NoSQLConnection noSQLConnection;

    @PostConstruct
    public void init() {
        System.out.println("DB Connection Bean Created with dependencies below:");
        System.out.println("is MySQLConnection object Null: " + Objects.isNull(mySQLConnection));
        System.out.println("is NoSQLConnection object Null: " + Objects.isNull(noSQLConnection));
    }
}
```
```java
@Component
public class NoSQLConnection {
    NoSQLConnection() {
        System.out.println("initialization of NoSQLConnection Bean");
    }
}

```
```java
@Component
public class MySQLConnection {
    MySQLConnection() {
        System.out.println("initialization of MySQLConnection Bean");
    }
}
```
### Use Cases
**Use Case 1:**  
We want to create only 1 Bean, either `MySQLConnection` or `NoSQLConnection`.

**Use Case 2:**  
We have 2 components sharing the same codebase, but 1 component needs `MySQLConnection` and the other needs `NoSQLConnection`.

### Conditional Bean Creation

To conditionally create beans based on properties, you can use the `@ConditionalOnProperty` annotation. Below is an example of how to implement this in your Spring application:

```java
@Component
@ConditionalOnProperty(prefix = "sqlConnection", value = "enabled", havingValue = "true", matchIfMissing = false)
public class MySQLConnection {
    MySQLConnection() {
        System.out.println("initialization of MySQLConnection Bean");
    }
}
```

```java
@Component
@ConditionalOnProperty(prefix = "noSQLConnection", value = "enabled", havingValue = "true", matchIfMissing = false)
public class NoSQLConnection {
    NoSQLConnection() {
        System.out.println("initialization of NoSQLConnection Bean");
    }
}
```

```java
@Component
public class DBConnection {
    @Autowired(required=false)//by default required is true
    MySQLConnection mySQLConnection;

    @Autowired(required=false)
    NoSQLConnection noSQLConnection;

    @PostConstruct
    public void init() {
        System.out.println("DB Connection Bean Created with dependencies below:");
        System.out.println("is MySQLConnection object Null: " + Objects.isNull(mySQLConnection));
        System.out.println("is NoSQLConnection object Null: " + Objects.isNull(noSQLConnection));
    }
}
```

### Application Properties

To enable the desired connection, you can set the following property in your `application.properties` file:

```
sqlConnection.enabled=true
```

This setup allows you to control which connection bean is created based on the configuration, providing flexibility in your application's behavior.

### Advantages:
1. Toggling of Feature
2. Avoid cluttering Application context with un-necessary beans.
3. Save Memory
4. Reduce application Startup time

### Disadvantages:
1. Misconfiguration can happen.
2. Code Complexity when overused.
3. Multiple bean creation with the same Configuration brings confusion.
4. Complexity in managing.



