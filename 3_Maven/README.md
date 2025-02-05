# Maven

Maven is a powerful project management and build automation tool used primarily for Java projects. 

## Key Features

- **Build Automation**: Automates the build process including compiling, testing, and packaging
- **Dependency Management**: Automatically downloads and manages project dependencies
- **Documentation Generation**: Creates project documentation and reports
- **Standardized Project Structure**: Enforces a consistent project layout
- **Project Object Model (POM)**: Uses pom.xml as the core configuration file

## How Maven Works

Maven operates using the concept of Project Object Model (POM), defined in a `pom.xml` file. When executing Maven commands:

1. Maven searches for the `pom.xml` file in the current directory
2. Reads the configuration and dependencies
3. Downloads required dependencies from repositories
4. Executes the requested build phase or goal

## Common Maven Commands

- `mvn clean`: Cleans the project by deleting the target directory
- `mvn compile`: Compiles the source code
- `mvn test`: Runs unit tests
- `mvn package`: Creates the distributable package (e.g., JAR/WAR)
- `mvn install`: Installs the package in local repository
- `mvn deploy`: Deploys the package to remote repository

## Project Structure

A typical Maven project follows this structure:

```
project-root/
  ├── pom.xml
  ├── src/
  │   ├── main/
  │   │   ├── java/
  │   │   └── resources/
  │   └── test/
  │       ├── java/
  │       └── resources/
  └── target/
```

## Understanding pom.xml

The Project Object Model (POM) file is the core of a Maven project. Here are its key elements:

### 1. XML Schema Definition
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
```
This header defines the XML schema and namespace for Maven POM files. It ensures your POM file follows Maven's standard structure and validates against the official schema.

### 2. Project Coordinates
```xml
<groupId>com.example</groupId>    <!-- Organization identifier -->
<artifactId>my-project</artifactId>    <!-- Project name -->
<version>1.0-SNAPSHOT</version>    <!-- Project version -->
```
These coordinates uniquely identify your project in Maven repositories:
- `groupId`: Usually your organization's reversed domain name
- `artifactId`: The name of your project
- `version`: The current version (SNAPSHOT indicates a development version)

### 3. Properties
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```
Properties section defines project-wide variables and settings:
- `maven.compiler.source/target`: Specifies Java version for compilation
- `project.build.sourceEncoding`: Defines character encoding for source files

### 4. Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
Dependencies section lists all external libraries your project needs:
- `groupId/artifactId/version`: Identifies the specific dependency
- `scope`: Defines when the dependency is needed (test, compile, runtime, etc.)

### 5. Parent POM (Optional)
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
</parent>
```
Parent POM is used to inherit configurations from another project:
- Common in Spring Boot projects
- Provides pre-configured dependencies and plugins
- Enables version management across multiple modules

## Maven Build Lifecycle Phases
-If you want to run "package" phase all the phases before "package" will be executed automatically.
-And if you want run specific goal of a particular phase, then all the goals of previous phases + current phase goals before the one you defined will get run.
![Build Phases](/images/mavenbuildphases.png)
![Build Phases](/images/mvncompile.png)
