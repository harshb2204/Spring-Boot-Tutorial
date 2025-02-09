# Authentication and Authorization

## Authentication
Authentication is the process of verifying who someone is. It's like checking an ID card to confirm a person's identity.

### Common Authentication Methods:
1. Username/Password
2. Multi-Factor Authentication (MFA)
3. Biometric Authentication
4. Single Sign-On (SSO)
5. Token-based Authentication (like JWT)

## Authorization
Authorization is the process of verifying what specific applications, files, and data a user has access to. It happens after authentication and determines what an authenticated user can do.

### Authorization Concepts:
1. Role-Based Access Control (RBAC)
2. Access Control Lists (ACL)
3. Permission Levels
4. Scope-based Authorization

## Key Differences

| Authentication | Authorization |
|----------------|---------------|
| Verifies who the user is | Determines what resources a user can access |
| Happens first | Happens after authentication |
| Usually requires user input | Usually invisible to the user |
| Can be partially changed by the user (e.g., password) | Can only be changed by an administrator |

## Best Practices
1. Always implement both authentication and authorization
2. Use secure protocols (HTTPS)
3. Implement the principle of least privilege
4. Regular security audits
5. Proper session management
6. Secure password storage (hashing)
7. Rate limiting to prevent brute force attacks

## Common Implementation Tools
- JSON Web Tokens (JWT)
- OAuth 2.0
- OpenID Connect
- Passport.js (for Node.js)
- Spring Security (for Java)
- Django Authentication (for Python)

## JSON Web Tokens (JWT)
JSON Web Tokens use public/private key pairs that are mathematically related. The public key can only verify tokens, not create them. Signing the token means you can store information like user_id in the token, and it cannot be tampered with without having the private key.

### Advantages of JWT
1. **Easy Verification**: No datastore needed to store prerequisites for token verification
2. **Expiration Control**: Can set expiration date which allows consumers to verify based on TTL
3. **Performance**: Verifying tokens involves only CPU cycles, not any I/O from the user

### Disadvantages of JWT
1. **No Instant Invalidation**: Cannot invalidate or ban a user instantly because we have to mark the user_id or JWT id as banned in our datastore, or wait for token expiration since it's stored on client side
2. **Size Impact**: Adding new fields in JWT will increase its size and would have network impact if it has to be sent on each request

### Opaque Tokens
An alternative approach where it doesn't store any information like user_id in the token. Instead, it stores the primary key of the DB itself that points to the entry of the user having information about the user. In this case, we can use fast retrieval DBs like redis.

#### Advantages of Opaque Tokens
1. **Simple Authorization**: It is simple and effective in authorization also to get an idea of the resources that a user can access
2. **Network Efficiency**: It doesn't store the fields or used data in itself so having less network impact

#### Disadvantages of Opaque Tokens
1. **Infrastructure Requirements**: Might need to maintain a auth service or a dedicated server to do all the auth stuff which is not the case with JWT because JWT can be verified without maintaining any datastore

## Refresh and Access Tokens

### Access Tokens
- Short-lived tokens (usually 15-60 minutes)
- Used to authenticate API requests
- Carried in Authorization header
- Contains user identity and permissions
- Should be kept secure in memory only

### Refresh Tokens
- Long-lived tokens (days/weeks)
- Used to obtain new access tokens
- Should be stored securely (HTTP-only cookies)
- Can be revoked by the server
- Helps implement "Remember Me" functionality

### Token Flow
1. User logs in with credentials
2. Server provides both access and refresh tokens
3. Client uses access token for API requests
4. When access token expires:
   - Client uses refresh token to get new access token
   - If refresh token is valid, server issues new access token
   - If refresh token is expired, user must log in again

### Security Considerations
1. **Access Token Security**
   - Never store in localStorage/sessionStorage
   - Keep in memory only
   - Clear on page refresh/close

2. **Refresh Token Security**
   - Store in HTTP-only cookies
   - Include secure and sameSite flags
   - Implement token rotation
   - Maintain token blacklist for revoked tokens

3. **Best Practices**
   - Implement token rotation
   - Use short expiry for access tokens
   - Enable refresh token reuse detection
   - Maintain server-side session records
   - Implement proper logout mechanism

