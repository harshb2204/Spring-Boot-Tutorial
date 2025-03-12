# Critical Section

Code segment, where shared resources are being accessed and modified.

When multiple requests try to access this critical section, data inconsistency can happen.

Its solution is the usage of **TRANSACTION**:
- It helps to achieve ACID property.

A (Atomicity):
Ensures all operations within a transaction are completed successfully. If any operation fails, the entire transaction will get rolled back.

C (Consistency):
Ensures that the DB state before and after the transactions should be consistent only.

I (Isolation):
Ensures that, even if multiple transactions are running in parallel, they do not interfere with each other.

D (Durability):
Ensures that committed transactions will never be lost despite system failure or crash.

BEGIN_TRANSACTION:
- Debit from A
- Credit to B
if all success:
    COMMIT;
Else
    ROLLBACK;

END_TRANSACTION;

In Spring Boot, we can use the `@Transactional` annotation to manage transactions declaratively. This annotation ensures that the methods it decorates are executed within a transaction context, providing automatic rollback in case of exceptions and simplifying transaction management.

### @Transactional Annotation

- **At Class Level**: 
  - Transaction applied to all public methods.
  
  ```java
  @Component
  @Transactional
  public class CarService {
      public void updateCar() {
          // this method will get executed within a transaction
      }

      public void updateBulkCars() {
          // this method will get executed within a transaction
      }

      private void helperMethod() {
          // this method will not get affected by Transactional annotation.
      }
  }
  ```

- **At Method Level**: 
  - Transaction applied to a particular method only.
  
  ```java
  @Component
  public class CarService {
      @Transactional
      public void updateCar() {
          // this method will get executed within a transaction
      }

      public void updateBulkCars() {
          // this method will NOT be executed within a transaction
      }
  }
  ```

### Transaction Management in Spring Boot

Transaction Management in Spring Boot uses AOP:

1. **Pointcut Expression**: 
   - Uses a pointcut expression to search for methods with the `@Transactional` annotation, like:
     ```java
     @within(org.springframework.transaction.annotation.Transactional)
     ```

2. **Advice Execution**: 
   - Once the pointcut expression matches, it runs an "Around" type advice. 
   - The advice is:
     ```java
     invokeWithinTransaction method present in TransactionalInterceptor class.
     ```
![](/images/transactional.png)

Hierarchy of transaction managers
![](/images/transactionhierarchy.png)





In Spring Boot, transaction management can be implemented in two ways: **Declarative** and **Programmatic**.

## 1. Declarative Transaction Management  
Declarative transaction management uses annotations to define transaction boundaries. Spring Boot automatically selects the appropriate transaction manager based on the underlying **DataSource** (JDBC, JPA, etc.).  

### Example:  
```java
@Component
public class User {

    @Transactional
    public void updateUser() {
        System.out.println("UPDATE QUERY to update the user db values");
    }
}
```

### Advantages:  
- Simple and easy to implement.  
- Less boilerplate code.  
- Managed automatically by Spring Boot.  

---

## 2. Programmatic Transaction Management  
Programmatic transaction management allows explicit control over transactions via code. This approach is **flexible but harder to maintain**.  

### Example:  
```java
@Component
public class User {

    @Transactional
    public void updateUser() {
        // 1. Update DB  
        // 2. External API Call  
        // 3. Update DB  
    }
}
```

### Advantages:  
- Offers greater control over transactions.  
- Can handle complex transaction scenarios.  

### Disadvantages:  
- More verbose and difficult to maintain.  
- Requires manual handling of transaction boundaries.  


## Declarative Transaction Management in Spring Boot  

Declarative transaction management allows managing transactions using annotations, making the code cleaner and easier to maintain. Spring Boot automatically selects the appropriate transaction manager based on the underlying **DataSource** (JDBC, JPA, etc.).

## 1. Basic Example of Declarative Transaction Management  
Using the `@Transactional` annotation to define a transaction boundary.  

### Example:  
```java
@Component
public class User {

    @Transactional
    public void updateUser() {
        System.out.println("UPDATE QUERY to update the user db values");
    }
}
```

---

## 2. Configuring DataSource and Transaction Manager  
Spring Boot allows defining a custom `DataSource` and transaction manager. The following example demonstrates how to set up an **H2 in-memory database** and transaction manager:

### Example:  
```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager userTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```
## 2 Ways to Implement Transactions Programmatically

### Approach 1: Using `PlatformTransactionManager`

```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager userTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

```java
@Component
public class UserProgrammaticApproach1 {
    
    PlatformTransactionManager userTransactionManager;

    UserProgrammaticApproach1(PlatformTransactionManager userTransactionManager) {
        this.userTransactionManager = userTransactionManager;
    }

    public void updateUserProgrammatic() {
        TransactionStatus status = userTransactionManager.getTransaction(null);
        try {
            // SOME INITIAL SET OF DB OPERATIONS
            System.out.println("Insert Query run1");
            System.out.println("Update Query run1");
            userTransactionManager.commit(status);
        } catch (Exception e) {
            userTransactionManager.rollback(status);
        }
    }
}
```

---

### Approach 2: Using `TransactionTemplate`
- TransactionTemplate is just a wrapper of all the methods (commit, rollback)
- you need to provide a transaction manager to use to the transactiontemplate
```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager userTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager userTransactionManager) {
        return new TransactionTemplate(userTransactionManager);
    }
}
```

```java
@Component
public class UserProgrammaticApproach2 {
    
    TransactionTemplate transactionTemplate;

    public UserProgrammaticApproach2(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void updateUserProgrammatic() {
        TransactionCallback<TransactionStatus> dbOperationsTask = (TransactionStatus status) -> {
            System.out.println("Insert Query ran");
            System.out.println("Update Query ran");
            return status;
        };
        
        TransactionStatus status = transactionTemplate.execute(dbOperationsTask);
    }
}
```








