jpostal
=======
[![Build Status](https://travis-ci.org/openvenues/jpostal.svg?branch=master)](https://travis-ci.org/openvenues/jpostal)

These are the Java/JNI bindings to [libpostal](https://github.com/openvenues/libpostal), a fast, multilingual NLP library (written in C) for parsing/normalizing physical addresses around the world.

Automatic Platform Detection
----------------------------
This library automatically detects your platform and loads the correct native libraries at runtime.

### Maven/Gradle Usage (Recommended)

**Maven:**

```xml
<dependency>
    <groupId>com.matchory.packages</groupId>
    <artifactId>jpostal</artifactId>
    <version>2.0.0</version>
</dependency>
```

**Gradle:**

```gradle
implementation 'com.matchory.packages:jpostal:2.0.0'
```

The universal JAR includes native libraries for all supported platforms:

- Linux x86_64 and ARM64 (aarch64)
- macOS Apple Silicon (aarch64)

Usage
-----
To expand address strings into normalized forms suitable for geocoder queries:

```java
import com.matchory.packages.jpostal.AddressExpander;

// Singleton, libpostal setup is done in the constructor
AddressExpander e = AddressExpander.getInstance();
String[] expansions = e.expandAddress("Quatre vingt douze Ave des Champs-Élysées");
```

To parse addresses into components:

```java
import com.matchory.packages.jpostal.AddressParser;

// Singleton, parser setup is done in the constructor
AddressParser p = AddressParser.getInstance();
ParsedComponent[] components = p.parseAddress("The Book Club 100-106 Leonard St, Shoreditch, London, Greater London, EC2A 4RH, United Kingdom");

for (ParsedComponent c : components) {
    System.out.printf("%s: %s\n", c.getLabel(), c.getValue());
}
```

To use a libpostal installation with a datadir known at setup-time:

```java

import com.matchory.packages.jpostal.AddressParser;
import com.matchory.packages.jpostal.AddressExpander;

AddressExpander e = AddressExpander.getInstanceDataDir("/some/path");
AddressParser p = AddressParser.getInstanceDataDir("/some/path");
```

Build Process
-------------
The jpostal project uses a simplified build process with pre-built native bindings for all supported platforms. This eliminates cross-compilation issues and makes CI builds faster and more reliable.

### Supported Platforms

- **Linux x86_64** - Intel/AMD 64-bit
- **Linux ARM64** - ARM 64-bit (aarch64)
- **macOS ARM64** - Apple Silicon

### Compiling JNI extensions
To compile the JNI portion of jpostal, run the following command:

```bash
./build-native.sh
```

This will build the shared object files (.so on Linux, .dylib on Mac) for all supported platforms and update the library distribution files.

Compatibility
-------------
- Building jpostal is known to work on Linux and Mac OSX (including Mac silicon).
- Requires JDK 16 or later. Make sure JAVA_HOME points to JDK 21+.

Tests
-----
To run the tests:

```
./gradlew check
```

License
-------
The package is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).
