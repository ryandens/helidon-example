import gradle.kotlin.dsl.accessors._3967a7c13ca8fbc0913119936467ec52.compileJava
import gradle.kotlin.dsl.accessors._3967a7c13ca8fbc0913119936467ec52.sourceSets
import org.gradle.api.GradleException
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("com.ryandens.example.java-common-conventions")
}

// not a jib specific issue, but described well here: https://github.com/GoogleContainerTools/jib/issues/1488
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
