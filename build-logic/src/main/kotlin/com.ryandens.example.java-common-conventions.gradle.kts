plugins {
    java
    id("com.ryandens.example.base")
}

dependencies {
    testImplementation(enforcedPlatform(project(":java-test-platform")))
}

spotless {
    java {
        googleJavaFormat()
    }
}

testing {
    suites {
        // Configure the built-in test suite
        named<JvmTestSuite>("test") {
            // Use JUnit Jupiter test framework
            useJUnitJupiter()
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(27))
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.options.release.set(27)
}
