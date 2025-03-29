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


