import org.gradle.api.GradleException
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

plugins {
    id("com.ryandens.example.java-common-conventions")
}

// not a jib specific issue, but described well here: https://github.com/GoogleContainerTools/jib/issues/1488
// We mark the beans directory as part of the source set so that it renders properly as part of the IDE experience
sourceSets {
    named("main") {
        java {
            srcDir("src/main/beans")
        }
    }
}


// this allows us to treat the beans file specially and removes the need for the workarounds described in the
// jib issue and described here https://github.com/helidon-io/helidon/blob/4.0.3/examples/quickstarts/helidon-quickstart-mp/build.gradle#L97
// in a way that lets helidon tests and jib have the beans.xml file in a location that is compatible with weld
// ideally, weld would look in any classpath entry for the beans.xml but that is far outside the scope of this repository
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
