pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version "4.6.1"
    id("com.ryandens.temurin-binaries-repository") version "0.9.0"
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

develocity {
    buildScan {
        val ci = providers.environmentVariable("CI")
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
        uploadInBackground.set(ci.map { false }.orElse(true))
        publishing.onlyIf { ci.isPresent }
    }
}

rootProject.name = "helidon-example"
include("app", "java-test-platform")
