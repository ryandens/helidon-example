plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

spotless {
    kotlinGradle {
        target("*.gradle.kts", "src/main/kotlin/*.gradle.kts")
        ktlint()
    }
}

dependencies {
    implementation(libs.spotless)
    implementation(libs.jandexPlugin)
    implementation(libs.jlinkPlugin)
    implementation(libs.jlinkJibPlugin)
    implementation(libs.temurinBinaries)
    implementation(libs.jibPlugin)
}

repositories {
    gradlePluginPortal()
}
