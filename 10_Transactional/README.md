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
