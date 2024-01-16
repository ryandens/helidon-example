plugins {
    id("com.ryandens.example.java-application-conventions")
    id("com.ryandens.example.weld-conventions")
    id("com.ryandens.example.helidon-conventions")
    id("com.ryandens.example.jlink-helidon-conventions")
    id("com.ryandens.jlink-application")
}

application {
    mainClass.set("io.helidon.microprofile.cdi.Main")
}
