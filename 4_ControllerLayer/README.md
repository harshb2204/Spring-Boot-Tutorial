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



