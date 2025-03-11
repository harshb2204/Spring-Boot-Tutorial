# Transaction Isolation

Isolation levels define the degree to which a transaction must be isolated from the data modifications made by any other transaction in the database system.

Spring Boot provides support for different isolation levels through the @Transactional annotation.

In order to understand isolation levels, we first need to look at some of the problems that occur in the database. Because isolation levels are a technology developed against these problems that we will talk about below.

# Dirty Read

Dirty Read - A Dirty read is a situation when a transaction reads data that has not yet been committed.

![](/images/dirtyread.png)

# Non Repeatable Read

Non Repeatable read - Non Repeatable read occurs when a transaction reads the same row twice and gets a different value each time.

![](/images/nonrepeatableread.png)

# Phantom Read

Phantom Read occurs when two same queries are executed, but the rows retrieved by the two are different.

![](/images/phantomread.png)

# Isolation Levels

Isolation levels are crucial for maintaining data integrity in concurrent transactions. The four main isolation levels defined by the SQL standard are:

1. **Read Uncommitted**: Allows transactions to read data that has been modified but not yet committed by other transactions. This can lead to dirty reads.

2. **Read Committed**: Ensures that any data read is committed at the moment it is read. This prevents dirty reads but allows non-repeatable reads.

3. **Repeatable Read**: Guarantees that if a transaction reads a row, it will see the same data if it reads that row again during the same transaction. This prevents both dirty reads and non-repeatable reads but can still allow phantom reads.

4. **Serializable**: The highest isolation level, which ensures complete isolation from other transactions. It prevents dirty reads, non-repeatable reads, and phantom reads by ensuring that transactions are executed in a serial order.

Choosing the appropriate isolation level is essential for balancing performance and data integrity based on the specific requirements of your application.
