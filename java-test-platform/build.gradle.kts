plugins {
    id("java-platform")
}

javaPlatform.allowDependencies()

dependencies {
    api(platform(libs.junit))
}