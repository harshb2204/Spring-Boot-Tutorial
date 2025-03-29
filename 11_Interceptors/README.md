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


