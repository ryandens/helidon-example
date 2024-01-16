plugins {
    id("com.ryandens.example.java-application-conventions")
    id("com.github.vlsi.jandex")
    id("com.ryandens.jlink-application")
}

dependencies {
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:4.0.2"))
    implementation("io.helidon.microprofile.bundles:helidon-microprofile")
    implementation("org.glassfish.jersey.media:jersey-media-json-binding")
    runtimeOnly("io.smallrye:jandex")
    runtimeOnly("jakarta.activation:jakarta.activation-api")
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-junit5")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

jlinkJre {
    modules = listOf("java.logging", "java.xml", "java.naming", "java.desktop", "java.management", "java.sql", "jdk.unsupported")
}

sourceSets {
    named("main") {
        java {
            srcDir("src/main/beans")
        }
    }
}

tasks.register<Copy>("processBeansXml") {
    from(layout.projectDirectory.file("src/main/beans/beans.xml"))
    into(layout.buildDirectory.dir("classes/java/main/META-INF"))
    doLast {
        if (inputs.sourceFiles.isEmpty) {
            throw GradleException("No beans.xml found in ${inputs.files}")
        }
    }
}

tasks.compileJava {
    dependsOn("processBeansXml")
}

application {
    mainClass.set("io.helidon.microprofile.cdi.Main")
}
