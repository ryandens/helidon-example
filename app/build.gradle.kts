import com.ryandens.example.JavaagentVersionUtil

plugins {
    id("com.ryandens.example.helidon-application-conventions")
    id("com.ryandens.example.helidon-jib-conventions")
    id("com.ryandens.javaagent-test") version "0.8.0"
}

dependencies {
    testImplementation(platform("org.mockito:mockito-bom:5.18.0"))
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-mocking")
    testImplementation("org.mockito:mockito-core")
    testJavaagent("org.mockito:mockito-core") {
        version(JavaagentVersionUtil.fromDependencyAndConfiguration(this, configurations.testRuntimeClasspath))
    }
}
