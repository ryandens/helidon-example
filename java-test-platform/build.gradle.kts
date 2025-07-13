plugins {
    id("com.ryandens.example.base")
    id("java-platform")
}

javaPlatform.allowDependencies()

dependencies {
    api(platform(libs.junit))
}
