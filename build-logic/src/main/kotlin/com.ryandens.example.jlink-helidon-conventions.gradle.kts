plugins {
    id("com.ryandens.jlink-jre")
}

// list of modules required my helidon applications
jlinkJre {
    modules =
        listOf(
            "java.desktop",
            "java.logging",
            "java.management",
            "java.naming",
            "java.sql",
            "java.xml",
            "jdk.unsupported",
        )
}
