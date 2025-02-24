# Caching

Caching allows you to efficiently
reuse previously retrieved or
computed data.
In computing, a cache is a high-speed data storage layer which
stores a subset of data, typically
transient in nature, so that future
requests for that data are served up
faster than is possible by accessing
the data's primary storage location.
![](/images/caching.png)

## DNS Caching

• **DNS: Domain to IP Resolution**  
When a user requests a website, their system or browser queries DNS
servers to translate the domain name into an IP address. Caching these
results means that repeated requests for the same domain are resolved
faster, reducing the need for repetitive network lookups and improving
overall browsing efficiency. This is particularly beneficial for reducing latency and enhancing user
experience by avoiding delays in domain resolution. 

### Why is DNS Caching Important?
Caching DNS results significantly improves browsing efficiency and reduces latency. Without caching, every visit to a website would require a fresh lookup, slowing down internet access.

- **Local Cache:** Your device stores recently used domain-IP mappings.
- **Browser Cache:** Modern browsers maintain their own DNS cache.
- **ISP DNS Cache:** ISPs cache DNS queries for frequently visited domains.

This helps:
✅ Reduce load on DNS servers  
✅ Speed up web browsing  
✅ Improve overall user experience  

## Web Caching

What is Web Caching?  
Web caching is a technique that stores copies of frequently accessed web content (e.g., HTML pages, images, CSS files, JavaScript, and videos) to serve users more quickly and efficiently. Instead of fetching data from the origin server every time a request is made, the cache delivers the stored content, reducing retrieval time.

### How Does Web Caching Work?
1. **User Requests a Web Page:**
   - A user visits a website (e.g., www.example.com).
   - Their request is first checked against cached data.

2. **Cache Hit (Fast Response):**
   - If the requested content is already cached, it is served immediately from the cache without contacting the origin server.

3. **Cache Miss (Fetching from Origin):**
   - If the content is not cached or has expired, the request is sent to the origin web server.
   - The server responds with the content, and the cache stores a copy for future requests.

4. **Serving Cached Content for Future Requests:**
   - When another user requests the same content, it is served from the cache, reducing server load and improving response time.

### Types of Web Caching
- **Browser Cache:**  
  The user's browser stores cached resources (e.g., CSS, images, JavaScript) to load websites faster on repeat visits.

- **Content Delivery Network (CDN) Cache:**  
  CDNs store cached copies of web content across multiple geographically distributed servers. When a user requests content, the nearest CDN server delivers it, reducing latency.

## Application Caching

Application caching stores frequently accessed data and computed results closer to the application, reducing the need for repetitive database queries or complex calculations.

### How It Works:
1. **Cache Lookup:** The application first checks the cache (e.g., Redis, Memcached) for requested data.
2. **Cache Hit:** If found, data is retrieved instantly, avoiding a database query.
3. **Cache Miss:** If not found, data is fetched from the database, stored in the cache, and then returned.

### Benefits:
✅ Faster Response Times – Reduces latency by avoiding repeated computations.  
✅ Lower Database Load – Minimizes query executions, improving efficiency.  
✅ Scalability – Helps handle high traffic without degrading performance.  
✅ Better User Experience – Speeds up application interactions.  

### Common Implementations:
- **Redis** – In-memory key-value store with persistence options.
- **Memcached** – Lightweight, high-speed caching system for transient data.
