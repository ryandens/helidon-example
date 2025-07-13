plugins {
    java
    id("com.ryandens.example.base")
}

dependencies {
    testImplementation(platform(project(":java-test-platform")))
}

spotless {
    java {
        googleJavaFormat()
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter()
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    this.options.release.set(21)
}
