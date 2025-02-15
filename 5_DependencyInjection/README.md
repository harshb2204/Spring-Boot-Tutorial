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

## Common issues dealing with dependency injection
1. Circular Dependency
![](/images/circulardependency.png)

### Solutions to it
