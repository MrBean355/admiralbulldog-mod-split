import org.gradle.api.Plugin
import org.gradle.api.Project

class ModPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create("mod", ModExtension::class.java)

        target.tasks.register("buildMod", BuildModTask::class.java) {
            it.replacements = extension.allReplacements
            it.fileRenames = extension.allFileRenames
            it.emoticons = extension.allEmoticons
        }

        target.tasks.register("publishMod", PublishModTask::class.java) {
            it.modName = extension.modName
            it.modDescription = extension.modDescription
        }
    }
}