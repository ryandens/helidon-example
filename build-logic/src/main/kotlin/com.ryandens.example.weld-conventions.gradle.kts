plugins {
    id("com.ryandens.example.java-common-conventions")
}

// Keep the descriptor visible as a source directory in the IDE.
sourceSets.main {
    java.srcDir("src/main/beans")
}

// Weld needs beans.xml in the same classpath entry as the compiled classes.
// Let compileJava own the whole output directory, including this descriptor.
tasks.compileJava {
    val beansXml = layout.projectDirectory.file("src/main/beans/beans.xml")
    inputs.file(beansXml).withPropertyName("beansXml").withPathSensitivity(PathSensitivity.NONE)
    doLast {
        val destination = destinationDirectory.file("META-INF/beans.xml").get().asFile
        destination.parentFile.mkdirs()
        beansXml.asFile.copyTo(destination, overwrite = true)
    }
}
