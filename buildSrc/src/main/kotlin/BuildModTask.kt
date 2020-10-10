import compile.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import util.Environment
import util.exec
import java.io.File

private const val COMPILED_DIR = "compiled"
private const val OUTPUT_VPK = "pak01_dir.vpk"

open class BuildModTask : DefaultTask() {
    var replacements: Map<String, String> = emptyMap()
    var fileRenames: Map<String, String> = emptyMap()
    var emoticons: Map<String, Int> = emptyMap()

    @TaskAction
    fun run() {
        Environment.init(project.properties)

        val projectOutput = project.file(COMPILED_DIR).also {
            if (Environment.fullCompile) {
                it.deleteRecursively()
            }
            it.mkdirs()
        }

        project.replaceStrings(projectOutput, replacements)
        replaceEmoticons(projectOutput, emoticons)

        if (!Environment.fullCompile) {
            exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)
            return
        }

        val contentDir = File(Environment.dotaRoot, "content/dota_addons/${project.name}").also {
            it.deleteRecursively()
            it.mkdirs()
        }
        val compilerOutput = File(Environment.dotaRoot, "game/dota_addons/${project.name}").also {
            it.deleteRecursively()
            it.mkdirs()
        }

        project.projectDir.listFiles().orEmpty()
                .filter { it.isDirectory && it.name != COMPILED_DIR }
                .forEach { it.copyRecursively(File(contentDir, it.name)) }

        compileImages(contentDir, compilerOutput)
        compileSounds(contentDir)
        compileMaterials(contentDir)
        project.renameFiles(compilerOutput, fileRenames)

        compilerOutput.copyRecursively(projectOutput)
        exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)

        if (Environment.fullCompile) {
            val destination = File(Environment.dotaRoot, "game/${project.name}/$OUTPUT_VPK")
            File(projectOutput, OUTPUT_VPK).copyTo(destination, overwrite = true)
        }

        contentDir.deleteRecursively()
        compilerOutput.deleteRecursively()
    }
}