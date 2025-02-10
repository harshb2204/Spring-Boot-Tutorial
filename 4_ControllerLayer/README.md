# Controller Layer Annotations

## Core Annotations

### @Controller
- Indicates that the class is responsible for handling incoming HTTP requests
- Used for traditional web applications with view resolution

### @RestController
- Combination of `@Controller` + `@ResponseBody`
- Used for building RESTful web services
- All responses are automatically serialized to JSON/XML

### @ResponseBody
- Denotes that return value of the controller method should be serialized to HTTP response body
- Not needed when using `@RestController`
- Without this, Spring treats the response as a view name for rendering

## Request Mapping

### @RequestMapping
Maps web requests to specific handler methods with various configuration options:

| Parameter | Description | Example |
|-----------|-------------|---------|
| value/path | URL path to map the request to | `/api/users` |
| method | HTTP method | `GET`, `POST`, `PUT`, `DELETE` |
| consumes | Accepted request content types | `application/json` |
| produces | Response content types | `application/json` |

**Note:** Can be applied at both class and method level

### Specialized Request Mapping Annotations
- `@GetMapping`: Shortcut for `@RequestMapping(method = RequestMethod.GET)`
- `@PostMapping`: Shortcut for `@RequestMapping(method = RequestMethod.POST)`
- `@PutMapping`: Shortcut for `@RequestMapping(method = RequestMethod.PUT)`
- `@DeleteMapping`: Shortcut for `@RequestMapping(method = RequestMethod.DELETE)`
- `@PatchMapping`: Shortcut for `@RequestMapping(method = RequestMethod.PATCH)`


## Code Examples

### Traditional Approach
```java
@Controller
public class SampleController {
    @RequestMapping(path = "/fetchUser", method = RequestMethod.GET)
    @ResponseBody
    public String getUserDetails() {
        return "fetching and returning user details";
    }

    @RequestMapping(path = "/saveUser", method = RequestMethod.POST)
    @ResponseBody
    public String saveUserDetails() {
        return "Successfully saved the user details";
    }
}
```

### Modern Approach (Using @RestController)
```java
@RestController
@RequestMapping("/api")
public class SampleController {
    @GetMapping("/fetchUser")
    public String getUserDetails() {
        return "fetching and returning user details";
    }

    @PostMapping("/saveUser")
    public String saveUserDetails() {
        return "Successfully saved the user details";
    }
}
```

## Best Practices
1. Use specialized mapping annotations (`@GetMapping`, `@PostMapping`, etc.) for better readability
2. Define base URL paths at the class level using `@RequestMapping`
3. Use `@RestController` for RESTful APIs instead of `@Controller` with `@ResponseBody`
4. Specify content types using `produces` and `consumes` when working with specific media types


## Request Parameters

### @RequestParam
- Used to bind request parameters to controller method parameters
- Can extract values from query parameters, form data, or multipart requests
- Supports optional parameters and default values

Example:
```java
@RestController
@RequestMapping(value = "/api")
public class SampleController {
    @GetMapping(path = "/fetchUser")
    public String getUserDetails(@RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName", required = false) String lastName,
                               @RequestParam(name = "age") int age) {
        return "fetching and returning user details based on first name = " + firstName + 
               ", lastName = " + lastName + " and age is = " + age;
    }
}
```

Sample URL:
```
http://localhost:8080/api/fetchUser?firstName=SHRAYANSH&lastName=JAIN&age=32
```

Key features of @RequestParam:
- `name`: Specifies the request parameter name to bind to
- `required`: Indicates if parameter is mandatory (defaults to true)
- `defaultValue`: Provides default value if parameter is missing
- Automatic type conversion (e.g., String to int)

## Custom Property Editors

### @InitBinder
- Used to customize request parameter binding
- Allows registration of custom property editors
- Helps in data type conversion and formatting

Example of using PropertyEditor:
```java
@RestController
@RequestMapping(value = "/api")
public class SampleController {
    
    @InitBinder
    protected void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, "firstName", new FirstNamePropertyEditor());
    }

    @GetMapping(path = "/fetchUser")
    public String getUserDetails(@RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName", required = false) String lastName,
                               @RequestParam(name = "age") int age) {
        return "fetching and returning user details based on first name = " + firstName + 
               ", lastName = " + lastName + " and age is = " + age;
    }
}

public class FirstNamePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text.trim().toLowerCase());
    }
}
```

Key features of PropertyEditor:
- Allows custom data transformation before binding
- Can be registered for specific fields or types
- Useful for formatting, validation, and data normalization
- Common use cases include:
  - Date format conversion
  - String normalization
  - Custom type conversion
  - Input sanitization

## Path Variables

### @PathVariable
- Used to extract values from the URL path and bind them to controller method parameters
- Enables RESTful URL patterns with dynamic path segments
- Supports type conversion from path segment to method parameter type

Example:
```java
@RestController
@RequestMapping(value = "/api")
public class SampleController {
    @GetMapping(path = "/fetchUser/{firstName}")
    public String getUserDetails(@PathVariable(value = "firstName") String firstName) {
        return "fetching and returning user details based on first name = " + firstName;
    }
}
```

Key features of @PathVariable:
- Extracts values from URI template patterns
- Can be used with multiple path variables in a single URL
- Supports optional values with `required` attribute
- Automatically converts path segments to appropriate data types
- URL pattern variables must match method parameter names (unless explicitly specified with `value` attribute)

Sample URL:
```
http://localhost:8080/api/fetchUser/SHRAYANSH
```

**Note:** Path variables are different from request parameters (@RequestParam) as they are part of the URL path rather than query parameters.

## Request Body

### @RequestBody
- Binds the HTTP request body to a controller method parameter
- Typically used for processing JSON/XML payloads in POST/PUT requests
- Automatically deserializes the request body into Java objects

Example:
```java
@RestController
@RequestMapping(value = "/api")
public class SampleController {
    @PostMapping(path = "/saveUser")
    public String getUserDetails(@RequestBody User user) {
        return "User created " + user.username + " - " + user.email;
    }
}

public class User {
    @JsonProperty("user_name")
    String username;
    String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

Sample CURL request:
```bash
curl --location --request POST 'http://localhost:8080/api/saveUser' \
--header 'Content-Type: application/json' \
--data-raw '{
    "user_name": "Shrayansh",
    "email": "sjxyztest@gmail.com"
}'
```

Key features of @RequestBody:
- Automatically converts JSON/XML to Java objects
- Works with complex nested objects
- Supports custom deserialization through Jackson annotations
- Can be combined with validation annotations
- Requires appropriate Content-Type header in the request

**Note:** When using @RequestBody, make sure your class has proper getters/setters or appropriate access levels for the properties you want to deserialize.

## Response Entity

### ResponseEntity<T>
- Represents the entire HTTP response including headers, status, and body
- Provides fine-grained control over the HTTP response
- Allows customization of response headers and status codes
- Type parameter `<T>` specifies the type of the response body

Example:
```java
@RestController
@RequestMapping(value = "/api")
public class SampleController {
    @GetMapping(path = "/fetchUser")
    public ResponseEntity<String> getUserDetails(@RequestParam(value = "firstName") String firstName) {
        String output = "fetched User details of " + firstName;
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
```

Key features of ResponseEntity:
- Complete control over HTTP response
- Flexible response building using builder pattern
- Support for custom headers and status codes
- Type-safe response body handling
- Common use cases include:
  - Custom status codes
  - Adding response headers
  - Conditional responses
  - Error handling
  - File downloads

Common ResponseEntity patterns:
```java
// Success response with body
return ResponseEntity.ok(data);

// Created response with location header
return ResponseEntity.created(location).body(data);

// No content response
return ResponseEntity.noContent().build();

// Custom status with headers
return ResponseEntity
    .status(HttpStatus.ACCEPTED)
    .header("Custom-Header", "value")
    .body(data);
```

**Note:** ResponseEntity is particularly useful when you need to customize the HTTP response beyond just the response body, such as in REST APIs that need to conform to specific HTTP semantics.



