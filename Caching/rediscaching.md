# Redis Caching in Spring Boot

Using Redis for caching in Spring Boot offers several advantages over the default caching mechanisms, such as ConcurrentHashMap-based caching or SimpleCacheManager. Here's why Redis is a better choice:

## 1. Distributed Caching
- The default caching in Spring Boot (like ConcurrentHashMap) is in-memory and local to the application instance.
- Redis, being an in-memory database, allows caching at a centralized location, making it ideal for distributed applications (e.g., microservices).

## 2. Persistence
- Default caching is volatile—it gets wiped out when the application restarts.
- Redis supports persistence via RDB snapshots and AOF (Append-Only File) logging, ensuring that cached data is not lost on restarts.

## 3. Scalability
- Redis can scale horizontally with clustering and vertically with replication, whereas default caching is limited by heap size.
- Large datasets in default caching can cause OutOfMemoryErrors, while Redis efficiently handles larger caches.

## 4. Serialization Requirements
- Redis stores data in binary format for efficiency
- When storing Java objects in Redis, they must be serialized (converted to bytes)
- When retrieving objects from Redis, they must be deserialized (converted back to Java objects)
- Classes that need to be cached must implement the `Serializable` interface
- Without implementing `Serializable`, Redis will throw `SerializationException` when attempting to cache the object

![](/images/rediscli.png)
## Setting Up Redis Cache

### 1. Dependencies
Add the following dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
```properties
spring.cache.type=redis
spring.data.redis.host=your-redis-host
spring.data.redis.port=your-redis-port
spring.data.redis.password=your-redis-password
spring.data.redis.ssl.enabled=false
```

