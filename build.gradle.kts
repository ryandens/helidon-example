val lifecycleTasks =
    listOf(
        LifecycleBasePlugin.ASSEMBLE_TASK_NAME,
        LifecycleBasePlugin.BUILD_TASK_NAME,
        LifecycleBasePlugin.CHECK_TASK_NAME,
        LifecycleBasePlugin.CLEAN_TASK_NAME,
        "spotlessApply",
    )

for (task in lifecycleTasks) {
    tasks.register(task) {
        group = "lifecycle"
        description = "Runs the $task task for all included builds."
        dependsOn(gradle.includedBuilds.map { it.task(":$task") })
    }
}