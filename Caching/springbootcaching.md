![](/images/springbootcaching.png)
Spring boot provides a Cache Abstraction API that allow us to use
different cache providers to cache objects.

1. @EnableCaching  
It is a class level annotation to enable caching in spring boot application. By default it setup a CacheManager and creates in-memory cache using one concurrent HashMap.

2. @Cacheable  
It is a method level annotation. It is used in the method whose response is to be cached. The Spring boot manages the request and response of the method to the cache that is specified in the annotation attribute.

3. @CachePut  
It is a method level annotation. It is used to update the cache after invoking the method. By doing this, the result is put in the cache and the method is executed. It has same attributes of @Cacheable annotation.

4. @CacheEvict  
It is a method level annotation. It is used to remove the data from the cache. When the method is annotated with this annotation then the method is executed and the cache will be removed / evicted.

