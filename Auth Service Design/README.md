## System Architecture and Flow

### Key Actors/Components

1. **Client**
   - Represents the user or application making requests
   - Stores and manages tokens locally

2. **Security Components**
   - `SecurityConfig`: Manages security configurations and access rules
   - `JWT Filter`: Intercepts requests to validate JWT tokens
   - `AuthenticationManager`: Validates user credentials during login

3. **Controllers**
   - `AuthController`: Handles user authentication and registration
   - `TokenController`: Manages token operations (issuing/refreshing)

4. **Services**
   - `JWTService`: Generates and validates JWTs
   - `RefreshTokenService`: Manages refresh token lifecycle
   - `UserDetailsServiceImpl`: Retrieves user details from database
   - `EventPublisher`: Publishes events to queue for processing

5. **Database**
   - Stores users, roles, tokens, and user-role mappings

### Detailed Flow

#### 1. Signup Flow (`/api/v1/signup`)
1. Client sends signup request
2. `AuthController` processes request
3. `UserDetailsServiceImpl` saves user to database
4. `EventPublisher` publishes event to queue
5. Response sent to client with status

#### 2. Login Flow (`/api/v1/login`)
1. Client sends credentials
2. `TokenController` validates via `AuthenticationManager`
3. If authenticated:
    - `JWTService` generates JWT and refresh token
    - `RefreshTokenService` saves refresh token
4. Both tokens returned to client

#### 3. Token Refresh Flow (`/api/v1/refreshToken`)
1. Client sends refresh token
2. `TokenController` verifies via `RefreshTokenService`
3. If valid:
    - `JWTService` generates new JWT
    - `UserDetailsServiceImpl` fetches user context
4. New JWT returned to client

#### 4. General Request Flow
1. `JWT Filter` intercepts request
2. Validates JWT token
3. If valid:
    - `UserDetailsServiceImpl` populates SecurityContext
    - Request proceeds to appropriate controller
4. If invalid:
    - Client receives authentication error

### Security Flow Diagram
![Authservice Design](/images/authservicedesign.png)