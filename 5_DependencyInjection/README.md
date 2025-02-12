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

## Conclusion
This highlights the importance of using Dependency Injection to maintain flexibility and reduce the need for changes in dependent classes while adhering to the Dependency Inversion Principle.