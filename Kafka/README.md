As we scale in a distributed system, we often find ourselves facing challenges such as:

- Handling large-scale data streams in distributed systems.
- Preventing tight coupling between services to allow independent evolution.
- Ensuring fault tolerance and preventing data loss.

In these scenarios, traditional request-response communication does not scale well. 
![](/images/dissystem.png)

Kafka is a horizontally scalable, fault-tolerant, and fast messaging system. It uses a publish-subscribe (pub-sub) model in which various producers and consumers can write and read messages. Kafka decouples source and target systems, enabling independent evolution and scaling.

Some of the key features of Kafka are:

- Scales to hundreds of nodes
- Can handle millions of messages per second
- Low latency: as low as 2ms
- High throughput: hundreds of MB/s with hundreds of thousands of messages per second
![](/images/kafka1.png)

Use cases of Kafka:

Kafka is widely used in the following scenarios:

- **Real-time data streaming:** Kafka is excellent for handling continuous streams of data, enabling immediate processing and analysis.
- **Log aggregation:** It can collect logs from various applications and servers into a central platform for monitoring and analysis.
- **Event sourcing:** Kafka can act as a persistent log of events, which can be replayed to reconstruct application states.
- **Messaging:** It serves as a high-throughput, low-latency messaging system for inter-application communication.
- **Batch data processing:** While primarily known for real-time, Kafka can also be used to collect data for later batch processing.

Do NOT use Kafka for:

There are scenarios where Kafka might not be the best fit:

- **Simple Request-Response Communication:** For straightforward, synchronous request-response interactions, traditional APIs or simpler messaging queues might be more appropriate. Kafka is designed for asynchronous, stream-based communication.
- **Small-Scale Projects:** The overhead and complexity of setting up and managing a Kafka cluster might be overkill for very small projects.
- **High Latency Tolerance:** While Kafka is low-latency, if an application can tolerate very high latencies (e.g., daily batch jobs that don't need immediate results), simpler solutions might suffice. Kafka shines when low latency is a requirement.
- **Monolithic Applications:** Kafka is typically used in distributed systems and microservices architectures to decouple components. For a tightly coupled monolithic application, its benefits might not be fully realized.


# Kafka Architecture
![](/images/kafka2.png)
Kafka is moving away from its dependency on ZooKeeper and using Kafka’s
KRaft (Kafka Raft) mode for maintaining brokers.    



## Key Concepts in Kafka
![](/images/kafka3.png)

- **Producer:** A producer is an application or service that publishes (writes) events or messages to Kafka topics. Producers are responsible for sending data to the Kafka cluster.

- **Consumer:** A consumer is an application or service that subscribes to Kafka topics and reads (consumes) events or messages from them. Consumers process the data produced by producers.

- **Events:** In Kafka, an event (or message or record) is a piece of data that is written to a topic. Each event typically consists of a key, a value, and metadata such as a timestamp.

- **Cluster:** A Kafka cluster is a group of Kafka brokers working together. The cluster manages the storage, distribution, and replication of data across multiple brokers to ensure scalability and fault tolerance.

- **Broker:** A broker is a single Kafka server that stores data and serves client requests (producers and consumers). Each broker in a cluster is responsible for a subset of partitions and topics, and brokers work together to balance load and ensure data availability.

# Kafka Topic

A topic is a specific stream of data. It is very similar to a table in a NoSQL database. Like tables in a NoSQL database, the topic is split into partitions that enable the topic to be distributed across various nodes. Like primary keys in tables, topics have offsets per partition. You can uniquely identify a message using its topic, partition, and offset.

Some topic names for an Ecommerce Application would be:

*order-placed, order-shipped, inventory-updated, payment-processed*

![](/images/kafkatopic.png)


# Topic Partitions

Kafka topics are subdivided into partitions, which are the basic unit of parallelism in Kafka. Each partition is an ordered, immutable sequence of records.

- Data within a partition is assigned a unique, incremental offset to track the order of messages.
- Partitions allow for scalability, as data can be distributed across multiple brokers, enabling horizontal scaling and concurrent consumption by multiple consumers.

![](/images/kafkapartition.png)


# Partition Offset

Offset is a unique identifier assigned to each record within a partition. Offsets are sequential integers that mark the position of a message in a partition. Each record within a partition has a unique offset, starting from 0.

Consumers can specify where they want to start reading by providing an offset (e.g., the most recent offset, or offset 0 to read from the beginning).

![](/images/partitionoffset.png)


# Consuming Topics

For a topic with multiple partitions, Kafka assigns a partition in a round-robin fashion, but users can also implement custom partitioning logic (e.g., based on a message key).

Consumers read messages from partitions, tracking the last offset they consumed.

Kafka does not automatically delete messages once they are consumed. It keeps messages based on a configured retention policy (e.g., 7 days or 100 GB per partition). Kafka provides the flexibility to reprocess messages by reading from an older offset.




