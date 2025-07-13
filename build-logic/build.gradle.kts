plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

spotless {
    kotlinGradle {
        target("*.gradle.kts", "src/main/kotlin/*.gradle.kts")
        ktlint()
    }
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint()
    }
}

dependencies {
    implementation(libs.spotless)
    implementation(libs.jandexPlugin)
    implementation(libs.jlinkJrePlugin)
    implementation(libs.jlinkPlugin)
    implementation(libs.jlinkJibPlugin)
    implementation(libs.temurinBinaries)
    implementation(libs.jibPlugin)
    implementation(libs.javaagentTest)
}

repositories {
    gradlePluginPortal()
}
