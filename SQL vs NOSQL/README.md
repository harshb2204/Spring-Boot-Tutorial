# Choosing DB: SQL vs NoSQL

## CAP Theorem and IOPS

The CAP theorem states that a distributed database system can only guarantee two out of these three properties at any given time:

1. **Consistency (C)**: Every read receives the most recent write or an error. In other words, all nodes in the system return the same, most recent, consistent value for a given piece of data.

2. **Availability (A)**: Every request receives a response, without guarantee that it contains the most recent write. This means that the system remains operational even in the face of network partitions or node failures.

3. **Partition tolerance (P)**: The system continues to operate despite network partitions (communication breakdowns) that might cause some messages to be lost or delayed.

## SQL Databases

### Consistency
SQL databases, especially those that follow ACID principles (Atomicity, Consistency, Isolation, Durability), prioritize strong consistency. When you perform a read operation, you're guaranteed to get the latest committed data.

### Availability
SQL databases can sacrifice availability in favor of consistency. In the face of network failures or node crashes, SQL databases may choose to block requests until consistency can be guaranteed, ensuring that clients receive the most recent and accurate data.

### Partition Tolerance
SQL databases typically prioritize consistency and availability over partition tolerance. While they can handle some network partitions, they often sacrifice partition tolerance to ensure data consistency and availability.

### Additional Characteristics
SQL databases are designed to support Complex queries and are usually good for write heavy operations.

## NoSQL Databases

### Consistency
NoSQL databases vary in their approach to consistency. Some NoSQL databases prioritize strong consistency, similar to SQL databases, while others may offer eventual consistency, where all replicas converge to the same state over time but may temporarily diverge.

### Availability
NoSQL databases often prioritize availability over consistency. In distributed NoSQL databases, individual nodes can continue to serve read and write requests even if they can't communicate with each other due to network partitions or failures. This ensures that the system remains operational even under adverse conditions.

### Partition Tolerance
NoSQL databases are designed to be highly partition-tolerant. They are built to continue operating even when network partitions occur, allowing nodes to operate independently and reconcile differences later.

### Additional Characteristics
It is suitable for read heavy ops because of its high availability and partition tolerance.
