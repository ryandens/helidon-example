plugins {
    java
    id("com.diffplug.spotless")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(project(":java-test-platform")))
}

spotless {
    java {
        googleJavaFormat()
    }
    kotlinGradle {
        ktlint()
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
