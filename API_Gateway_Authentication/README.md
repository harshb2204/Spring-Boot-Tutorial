# API Gateway Authentication

- The API Gateway acts as a central authentication point for all incoming requests.
Clients authenticate once with the API Gateway, which validates the credentials
(could be a JWT, OAuth2, or any other method).
The API Gateway forwards the request to the respective microservice with the user's
credentials or token.
 
![](/images/apigatewayauth.png)

## Authentication Gateway Filter Implementation

### Overview
The `AuthenticationGatewayFilterFactory` is a custom Spring Cloud Gateway filter that handles JWT-based authentication for incoming requests. It validates JWT tokens and extracts user information before forwarding requests to downstream microservices.

### Code Implementation

```java


@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if(!config.isEnabled) return chain.filter(exchange);

            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authorizationHeader == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.split("Bearer ")[1];

            Long userId = jwtService.getUserIdFromToken(token);

            exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .build();

            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private boolean isEnabled;
    }
}
```

### How It Works

1. **Filter Registration**: The filter is registered as a Spring component using `@Component` annotation.

2. **Configuration**: The filter accepts a `Config` object that contains an `isEnabled` flag to conditionally enable/disable authentication.

3. **Request Processing Flow**:
   - **Conditional Execution**: If authentication is disabled (`!config.isEnabled`), the request passes through without authentication.
   - **Header Extraction**: Extracts the `Authorization` header from the incoming request.
   - **Token Validation**: If no authorization header is present, returns a 401 Unauthorized response.
   - **Token Parsing**: Extracts the JWT token from the "Bearer " prefix.
   - **User ID Extraction**: Uses the `JwtService` to extract the user ID from the JWT token.
   - **Header Addition**: Adds the extracted user ID as `X-User-Id` header to the request.
   - **Request Forwarding**: Continues the filter chain to forward the request to downstream services.

### Key Components

- **JwtService**: Service responsible for JWT token validation and user ID extraction.
- **Config Class**: Configuration class with `isEnabled` flag for conditional authentication.
- **Gateway Filter**: Implements the actual authentication logic using Spring Cloud Gateway's filter mechanism.

### Usage in Application Configuration

To use this filter in your API Gateway routes, add it to your `application.yml` or `application.properties`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - Authentication=true
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - Authentication=true
```

### Benefits

1. **Centralized Authentication**: All authentication logic is centralized in the API Gateway.
2. **Microservice Decoupling**: Downstream microservices don't need to implement authentication logic.
3. **User Context Propagation**: User ID is automatically propagated to all microservices via headers.
4. **Flexible Configuration**: Can be enabled/disabled per route using the `isEnabled` configuration.
5. **Security**: Prevents unauthorized access to protected endpoints.

### Security Considerations

- Ensure the `JwtService` properly validates token signatures and expiration.
- Consider implementing token refresh mechanisms.
- Add proper error handling for malformed tokens.
- Implement rate limiting for authentication endpoints.
- Use HTTPS in production environments.

### Dependencies Required

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```
