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


