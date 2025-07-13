package com.ryandens.example

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ModuleVersionSelector
import org.gradle.api.artifacts.component.ModuleComponentIdentifier

object JavaagentVersionUtil {
    fun versionFromDependencyAndConfiguration(
        dependency: ModuleVersionSelector,
        configuration: NamedDomainObjectProvider<Configuration>,
    ): String {
        val artifact =
            configuration
                .get()
                .incoming
                .artifactView {
                    componentFilter {
                        it.displayName.startsWith("${dependency.group}:${dependency.name}")
                    }
                }.artifacts
                .single()
        return (artifact.id.componentIdentifier as ModuleComponentIdentifier).version
    }
}
