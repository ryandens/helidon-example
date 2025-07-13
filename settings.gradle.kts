pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
    id("com.gradle.develocity") version "4.0.2"
}

val isCI = providers.environmentVariable("CI").isPresent

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
        uploadInBackground.set(!isCI)

        publishing.onlyIf { isCI }
    }
}

rootProject.name = "helidon-example"
include("app")
