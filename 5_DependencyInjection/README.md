# What is Dependency Injection

## Introduction
- Dependency Injection (DI) is a design pattern that allows a class to receive its dependencies from an external source rather than creating them internally. This promotes loose coupling and enhances testability.

## Benefits of Dependency Injection
- **Decoupling**: Classes are less dependent on concrete implementations, making them easier to modify and extend.
- **Flexibility**: Changes to dependencies can be made without altering the dependent class.
- **Testability**: Dependencies can be easily mocked or stubbed during testing.

## Problem Statement
Let's examine a common problem with tightly coupled classes:

```java
public class User {
    Order order = new Order(); // Tightly coupled to Order

    public User(){
        System.out.println("initializing user");
    }
}

public class Order {
    public Order() {
        System.out.println("initializing Order");
    }
}
```

## Issues with Tightly Coupled Classes
- Tightly Coupled
   - Both `User` and `Order` classes are tightly coupled.
   - If the `Order` object creation logic changes (e.g., if `Order` becomes an interface with multiple implementations), the `User` class must also be modified.
![di](/images/di.png)

It breaks the Dependency Inversion rule of the S.O.L.I.D principle.

## Dependency Inversion Principle
- **Breaks Dependency Inversion Principle (DIP)**:
   ```java
   public class User {
       Order order = new OnlineOrder(); // Concrete implementation

       public User() {
           System.out.println("initializing user");
       }
   }
   ```

   **Follows Dependency Inversion Principle (DIP)**:
   ```java
   public class User {
       Order order;

       public User(Order orderObj) {
           this.order = orderObj; // Dependency injected
       }
   }
   ```

   This principle says that DO NOT depend on concrete implementation, rather depends on abstraction.

## Achieving Dependency Inversion Principle in Spring Boot

- Using Dependency Injection, we can make our class independent of its dependencies.
- It helps to remove the dependency on concrete implementation and inject the dependencies from an external source.

```java
@Component
public class User {
    @Autowired
    Order order;
}
```

```java
@Component
public class Order {
}
```

- `@Autowired` first looks for a bean of the required type.
  - If a bean is found, Spring will inject it.

## Dependency Resolution in Spring

- Dependency is set into the fields of the class directly.
- Spring uses reflection; it iterates over the fields and resolves the dependency.

```java
@Component
public class User {
    @Autowired
    Order order;

    public User() {
        System.out.println("User initialized");
    }
}
```

```java
@Component
@Lazy
public class Order {
    public Order() {
        System.out.println("order initialized");
    }
}
```

## Disadvantage of Field Injection

- Cannot be used with immutable fields.

```java
@Component
public class User {
    @Autowired
    public final Order order;

    public User() {
        System.out.println("User initialized");
    }
}
```

## Setter Injection

- Dependency is set into the fields using the setter method.
- We have to annotate the method using `@Autowired`.

```java
@Component
public class User {
    public Order order;

    public User() {
        System.out.println("User initialized");
    }

    @Autowired
    public void setOrderDependency(Order order) {
        this.order = order;
    }
}
```

```java
@Component
@Lazy
public class Order {
    public Order() {
        System.out.println("order initialized");
    }
}
```

### Advantage:
- Dependency can be changed any time after the object creation (as the object cannot be marked as final).
- Ease of testing, as we can pass mock objects in the dependency easily.

### Disadvantage:
- Field cannot be marked as final (we cannot make it immutable).

```java
@Component
public class User {
    public final Order order;

    public User() {
        System.out.println("User initialized");
    }

    @Autowired
    public void setOrderDependency(Order order) {
        this.order = order;
    }
}
```

- Difficult to read and maintain, as per standard, the object should be initialized during object creation, so this might create code readability issues.

## Constructor Injection

- Dependency gets resolved at the time of initialization of the object itself.
- It's recommended to use.

```java
@Component
public class User {
    Order order;

    @Autowired
    public User(Order order) {
        this.order = order;
        System.out.println("User initialized");
    }
}
```

```java
@Component
@Lazy
public class Order {
    public Order() {
        System.out.println("order initialized");
    }
}
```

## Constructor Overloading

When more than one constructor is present, using `@Autowired` on the constructor is mandatory.

```java
@Component
public class User {
    Order order;
    Invoice invoice;

    @Autowired
    public User(Order order) {
        this.order = order;
        System.out.println("User initialized with only Order");
    }

    @Autowired
    public User(Invoice invoice) {
        this.invoice = invoice;
        System.out.println("User initialized with only Invoice");
    }
}
```

```java
@Component
@Lazy
public class Invoice {
    public Invoice() {
        System.out.println("invoice initialized");
    }
}
```

```java
@Component
@Lazy
public class Order {
    public Order() {
        System.out.println("order initialized");
    }
}
```

## Why Constructor Injection is Recommended (Advantages):

1. **All mandatory dependencies are created at the time of initialization itself.** 
   - Makes 100% sure that our object is fully initialized with mandatory dependencies.
     - Avoid NPE during runtime.
     - Unnecessary null checks can be avoided too.

2. **We can create immutable objects using Constructor injection.**

3. **Fail Fast:** 
   - If there is any missing dependency, it will fail during compilation itself, rather than failing during runtime.

## Circular Dependency Solutions

1. **First and foremost, can we refactor the code and remove this cycle dependency:**

   For example, common code in which both are dependent can be taken out to a separate class. This way we can break the circular dependency.

## Using @Lazy on @Autowired Annotation

Spring will create a proxy bean instead of creating the bean instance immediately during application startup.

### @Lazy on Field Injection

Let's first consider this:

```java
@Component
@Lazy  // Added @Lazy here
public class Order {
    public Order() {
        System.out.println("Order initialized");
    }
}

@Component
public class Invoice {
    @Autowired
    public Order order; 

    public Invoice() {
        System.out.println("Invoice initialized");
    }
}
```

**Log Output during application startup:**
```
Invoice initialized
Order initialized 
```

Now, let's see this:

```java
@Component
public class Invoice {
    @Lazy // Added @Lazy here
    @Autowired
    public Order order;  // @Lazy moved here

    public Invoice() {
        System.out.println("Invoice initialized");
    }
}

@Component
public class Order {
    public Order() {
        System.out.println("Order initialized");
    }
}
```

**Log Output during application startup:**
```
Invoice initialized
```

### Explanation:

In the first example, `@Lazy` on the `Order` class makes the `Order` bean lazy-initialized. However, the `@Autowired` in `Invoice` still triggers the initialization of the `Order` bean during the creation of the `Invoice` bean, even if it's annotated with `@Lazy`. So, both "Invoice initialized" and "Order initialized" are printed during startup.

In the second example, `@Lazy` is placed directly on the `@Autowired` field in the `Invoice` class. This means that the injection of the `Order` bean into the `Invoice` bean is lazy. The `Order` bean is only initialized when it's first accessed (e.g., when a method of the `Invoice` bean that uses the `order` field is called). Therefore, only "Invoice initialized" is printed during startup. "Order initialized" will be printed later when the `order` bean is actually needed.

## Circular Dependency Example

### Order.java:

```java
@Component
public class Order {

    @Autowired
    Invoice invoice;

    public Order() {
        System.out.println("order initialized");
    }
}
```

### Invoice.java:

```java
@Component
public class Invoice {

    @Lazy
    @Autowired
    public Order order;

    public Invoice() {
        System.out.println("Invoice initialized");
    }
}
```

### Log Output:

```
Invoice initialized
order initialized
```

### Explanation:

- **Circular Dependency:** There's a circular dependency between `Order` and `Invoice`. `Order` has an `Invoice` dependency, and `Invoice` has an `Order` dependency. Without `@Lazy`, this would cause a `BeanCurrentlyInCreationException` during application startup because Spring would be stuck trying to create both beans simultaneously.

- **@Lazy to the Rescue:** The `@Lazy` annotation on the `Order` field in the `Invoice` class is key. It tells Spring to inject a proxy for the `Order` bean initially, rather than the actual `Order` bean itself. This breaks the circular dependency cycle during the bean creation phase.

### Initialization Order:

1. Spring first creates the `Invoice` bean. Because of `@Lazy`, it injects a proxy for the `Order` bean. The `Invoice` constructor prints "Invoice initialized".
2. Later, when the `Order` bean is actually needed (e.g., when a method of the `Invoice` bean is called that uses the `order` field), Spring will then create the actual `Order` bean. The `Order` constructor prints "order initialized".

### How it Resolves the Circular Dependency:

By using `@Lazy`, the creation of the `Order` bean is deferred. When Spring is creating the `Invoice` bean, it doesn't need to fully create the `Order` bean immediately. It just creates a proxy. This prevents the "chicken and egg" scenario where Spring is trying to create both beans at the same time, leading to the `BeanCurrentlyInCreationException`.

## UnsatisfiedDependencyException Example

### Problem:

The `User` class depends on the `Order` interface. There are two implementations of the `Order` interface: `OnlineOrder` and `OfflineOrder`. Spring doesn't know which implementation to inject into the `User` class.

### Solution:

To resolve this ambiguity, you can use the `@Primary` annotation.

### Code Snippets:

#### Order Interface:

```java
public interface Order {
    void processOrder();
}
```

#### OnlineOrder Implementation:

```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class OnlineOrder implements Order {
    @Override
    public void processOrder() {
        System.out.println("Processing online order");
    }
}
```

#### OfflineOrder Implementation:

```java
import org.springframework.stereotype.Component;

@Component
public class OfflineOrder implements Order {
    @Override
    public void processOrder() {
        System.out.println("Processing offline order");
    }
}
```

#### User Class:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class User {
    private final Order order;

    @Autowired
    public User(Order order) {
        this.order = order; // Spring will inject the OnlineOrder by default
        System.out.println("User initialized with order");
    }
}
```

### Explanation:

By marking the `OnlineOrder` implementation with `@Primary`, you tell Spring to prefer this implementation when injecting the `Order` dependency. This resolves the ambiguity and allows the application to start successfully.
