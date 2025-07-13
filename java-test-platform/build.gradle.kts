plugins {
    id("com.ryandens.example.base")
    id("java-platform")
}

javaPlatform.allowDependencies()

dependencies {
    api(enforcedPlatform(libs.junit))
    api(enforcedPlatform(libs.mockito))
}
