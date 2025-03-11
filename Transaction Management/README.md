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
