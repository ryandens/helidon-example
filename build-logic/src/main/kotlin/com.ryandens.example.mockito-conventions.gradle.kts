import com.ryandens.example.JavaagentVersionUtil

plugins {
    id("com.ryandens.example.java-common-conventions")
    id("com.ryandens.javaagent-test")
}

configurations.testJavaagent {
    resolutionStrategy {
        eachDependency {
            if (requested.version.isNullOrBlank()) {
                useVersion(JavaagentVersionUtil.versionFromDependencyAndConfiguration(requested, configurations.testRuntimeClasspath))
            }
        }
    }
}
