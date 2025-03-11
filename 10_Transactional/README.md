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

