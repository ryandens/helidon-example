plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

val functionalTest =
    testing.suites.register<JvmTestSuite>("functionalTest") {
        useJUnitJupiter()
        dependencies {
            implementation(platform(libs.junit))
            implementation(gradleTestKit())
        }
        gradlePlugin.testSourceSets(sources)
    }

tasks.check {
    dependsOn(functionalTest)
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
    implementation(libs.jibPlugin)
    implementation(libs.javaagentTest)
    implementation(libs.javaagentJibPlugin)
}
