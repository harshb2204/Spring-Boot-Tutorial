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

**Spring Transaction Propagation Types**

When we try to create a new transaction, we first check the **PROPAGATION** value set, which determines whether a new transaction needs to be created.

### **1. REQUIRED (Default Propagation)**
```java
@Transactional(propagation=Propagation.REQUIRED)
if(parent txn present) {
    Use it;
} else {
    Create new transaction;
}
```
- If a parent transaction exists, it is used.
- If there is no existing transaction, a new one is created.

### **2. REQUIRED_NEW**
```java
@Transactional(propagation=Propagation.REQUIRED_NEW)
if(parent txn present) {
    Suspend the parent txn;
    Create a new transaction and once finished;
    Resume the parent txn;
} else {
    Create new transaction and execute the method;
}
```
- Always creates a new transaction.
- If a parent transaction exists, it is suspended while the new transaction runs.

### **3. SUPPORTS**
```java
@Transactional(propagation=Propagation.SUPPORTS)
if(parent txn present) {
    Use it;
} else {
    Execute the method without any transaction;
}
```
- If a transaction exists, it is used.
- If no transaction exists, the method runs **without** a transaction.

### **4. NOT_SUPPORTED**
```java
@Transactional(propagation=Propagation.NOT_SUPPORTED)
if(parent txn present) {
    Suspend the parent txn;
    Execute the method without any transaction;
    Resume the parent txn;
} else {
    Execute the method without any transaction;
}
```
- If a transaction exists, it is **suspended** before execution.
- Always runs the method **without** a transaction.

### **5. MANDATORY**
```java
@Transactional(propagation=Propagation.MANDATORY)
if(parent txn present) {
    Use it;
} else {
    Throw exception;
}
```
- Requires an existing transaction.
- If no transaction is present, it throws an **exception**.

### **6. NEVER**
```java
@Transactional(propagation=Propagation.NEVER)
if(parent txn present) {
    Throw exception;
} else {
    Execute the method without any transaction;
}
```
- The method must **not** run within a transaction.
- If a transaction exists, it throws an **exception**.

Each propagation type serves a specific use case, ensuring precise control over transactional behavior in Spring applications.

## Isolation Level

Isolation level:
It tells how the changes made by one transaction are visible to other transactions running in parallel.

```java
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public void updateUser() {
    // some operations here
}
```

| Isolation Level      | Dirty Read Possible | Non-Repeatable Read Possible | Phantom Read Possible |
|----------------------|---------------------|-------------------------------|-----------------------|
| READ_UNCOMMITTED     | Yes                 | Yes                           | Yes                   |
| READ_COMMITTED       | No                  | Yes                           | Yes                   |
| REPEATABLE_COMMITTED  | No                  | No                            | Yes                   |
| SERIALIZABLE         | No                  | No                            | No                    |

**Concurrency High**  ↑  
**Concurrency Low**   ↓
- default isolation level depends upon the db we are using
- read_commited is the default in most relational db.
## Dirty Read Problem

Default isolation level depends upon the DB we are using. Like most relational databases use `READ_COMMITTED` as the default isolation, but it again depends upon the DB.

Transaction A reads the un-committed data of another transaction. If the other transaction gets ROLLED BACK, the un-committed data which is read by Transaction A is known as Dirty Read.

| Time | Transaction A | Transaction B | DB Status |
|------|---------------|----------------|-----------|
| T1   | BEGIN_TRANSACTION | BEGIN_TRANSACTION | Id: 123<br>Status: free |
| T2   | Update Row<br>id:123<br>Status = booked |  | Id: 123<br>Status: booked<br>(Not Committed by Transaction B yet) |
| T3   | Read Row<br>id:123<br>(Got status = booked) |  | Id: 123<br>Status: booked<br>(Not Committed by Transaction B yet) |
| T4   |  | Rollback | Id: 123<br>Status: Free<br>(Un-committed changes of Txn B got Rolled Back) |

## Non-Repeatable Read Problem

If Transaction A reads the same row several times and there is a chance that it gets a different value, then it is known as the Non-Repeatable Read problem.

| Time | Transaction A | DB |
|------|---------------|----|
| T1   | BEGIN_TRANSACTION | ID: 1<br>Status: Free |
| T2   | Read Row ID:1<br>(reads status: Free) | ID: 1<br>Status: Free |
| T3   |  | ID: 1<br>Status: Booked |
| T4   | Read Row ID:1<br>(reads status: Booked) | ID: 1<br>Status: Booked |
| T5   | COMMIT |  |

## Phantom Read Problem

If Transaction A executes the same query several times but there is a chance that the rows returned are different, then it is known as the Phantom Read problem.

| Time | Transaction A | DB |
|------|---------------|----|
| T1   | BEGIN_TRANSACTION | ID: 1<br>Status: Free<br>ID: 3<br>Status: Booked |
| T2   | Read Row where ID>0 and ID<5<br>(reads 2 rows ID:1 and ID:3) | ID: 1<br>Status: Free<br>ID: 3<br>Status: Booked |
| T3   |  | ID: 1<br>Status: Free<br>ID: 2<br>Status: Free<br>ID: 3<br>Status: Booked |
| T4   | Read Row where ID>0 and ID<5<br>(reads 3 rows ID:1, ID:2 and ID:3) | ID: 1<br>Status: Free<br>ID: 2<br>Status: Free<br>ID: 3<br>Status: Booked |
| T5   | COMMIT |  |

## DB Locking Types

Locking ensures that no other transaction updates the locked rows.

| Lock Type          | Another Shared Lock | Another Exclusive Lock |
|--------------------|---------------------|------------------------|
| Have Shared Lock (S) | Yes                 | No                     |
| Have Exclusive Lock (X) | No                  | No                     |

- Shared Lock (S) is also known as READ LOCK.
- Exclusive Lock (X) is also known as WRITE LOCK.

## Locking Strategy by Isolation Level

| ISOLATION LEVEL      | Locking Strategy                                      |
|----------------------|------------------------------------------------------|
| Read Uncommitted     | Read: No Lock acquired<br>Write: No Lock acquired    |
| Read Committed       | Read: Shared Lock acquired and Released as soon as Read is done<br>Write: Exclusive Lock acquired and kept till the end of the transaction |
| Repeatable Read      | Read: Shared Lock acquired and Released only at the end of the Transaction<br>Write: Exclusive Lock acquired and Released only at the end of the Transaction |
| Serializable         | Same as Repeatable Read Locking Strategy + apply Range Lock and lock is released only at the end of the Transaction. |










