plugins {
    id("com.ryandens.example.helidon-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":utilities"))
}
