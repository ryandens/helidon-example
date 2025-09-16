pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version "4.2"
}

val isCI = providers.environmentVariable("CI").isPresent

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
        uploadInBackground.set(!isCI)

        if (isCI) {
            publishing.onlyIf { true }
        } else {
            publishing.onlyIf { false }
        }
    }
}

rootProject.name = "helidon-example"
include("app", "java-test-platform")
