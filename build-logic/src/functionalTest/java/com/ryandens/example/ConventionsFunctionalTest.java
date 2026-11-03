package com.ryandens.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConventionsFunctionalTest {
    @TempDir Path projectDir;

    @Test
    void beansDescriptorIsPackagedCachedAndInvalidatedWhenChanged() throws IOException {
        fixture("com.ryandens.example.weld-conventions");
        String descriptor = "<beans bean-discovery-mode=\"all\"/>";
        write("app/src/main/beans/beans.xml", descriptor);

        run(":app:jar", "--build-cache", "--configuration-cache");
        assertPackagedDescriptor(descriptor);

        BuildResult repeated = run(":app:jar", "--build-cache", "--configuration-cache");
        assertEquals(TaskOutcome.UP_TO_DATE, repeated.task(":app:compileJava").getOutcome());
        assertTrue(repeated.getOutput().contains("Reusing configuration cache"));

        BuildResult restored = run(":app:clean", ":app:jar", "--build-cache");
        assertEquals(TaskOutcome.FROM_CACHE, restored.task(":app:compileJava").getOutcome());
        assertPackagedDescriptor(descriptor);

        String changed = "<beans bean-discovery-mode=\"annotated\"/>";
        write("app/src/main/beans/beans.xml", changed);
        BuildResult updated = run(":app:jar", "--build-cache", "--configuration-cache");
        TaskOutcome outcome = updated.task(":app:compileJava").getOutcome();
        assertTrue(outcome == TaskOutcome.SUCCESS || outcome == TaskOutcome.FROM_CACHE);
        assertPackagedDescriptor(changed);
    }

    @Test
    void containerGraphIncludesIndexAndLinuxRuntimeProducers() throws IOException {
        fixture("com.ryandens.example.helidon-jib-conventions");
        BuildResult graph = run(":app:jibBuildTar", "--dry-run");
        String output = graph.getOutput();
        for (String producer : List.of("compileJava", "processJandexIndex", "copyJdks", "linuxJlinkJre")) {
            int producerPosition = output.indexOf(":app:" + producer + " SKIPPED");
            assertTrue(producerPosition >= 0, output);
            assertTrue(producerPosition < output.indexOf(":app:jibBuildTar SKIPPED"), output);
        }
    }

    private void fixture(String plugin) throws IOException {
        write("settings.gradle", """
                plugins {
                    id 'org.gradle.toolchains.foojay-resolver-convention' version '1.0.0'
                    id 'com.ryandens.temurin-binaries-repository' version '0.9.0'
                }
                dependencyResolutionManagement {
                    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
                    repositories { mavenCentral() }
                }
                rootProject.name = 'conventions-test'
                include 'app', 'java-test-platform'
                """);
        write("gradle.properties", "org.gradle.jvmargs=-Dfile.encoding=UTF-8\n");
        write("java-test-platform/build.gradle", "plugins { id 'java-platform' }\n");
        write("app/build.gradle", "plugins { id '" + plugin + "' }\n");
        write("app/src/main/java/example/Example.java", "package example; public class Example {}\n");
    }

    private BuildResult run(String... arguments) {
        List<String> options = new ArrayList<>(Arrays.asList(arguments));
        options.add("--console=plain");
        options.add("--stacktrace");
        return GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
                .withArguments(options)
                .build();
    }

    private void assertPackagedDescriptor(String expected) throws IOException {
        assertEquals(expected, Files.readString(projectDir.resolve("app/build/classes/java/main/META-INF/beans.xml")));
        try (JarFile jar = new JarFile(projectDir.resolve("app/build/libs/app.jar").toFile())) {
            assertNotNull(jar.getEntry("example/Example.class"));
            var entry = jar.getEntry("META-INF/beans.xml");
            assertNotNull(entry);
            try (var input = jar.getInputStream(entry)) {
                assertEquals(expected, new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
            }
        }
    }

    private void write(String relativePath, String content) throws IOException {
        Path path = projectDir.resolve(relativePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
}
