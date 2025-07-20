import compile.compileImages
import compile.compileMaterials
import compile.compileSounds
import compile.renameFiles
import compile.replaceEmoticons
import compile.replaceStrings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import util.Environment
import util.exec
import java.io.File

private const val COMPILED_DIR = "compiled"
private const val OUTPUT_VPK = "pak01_dir.vpk"
private const val INSTALL_DIR = "game/dota_bulldog"

open class BuildModTask : DefaultTask() {
    @Input
    var replacements: Map<String, String> = emptyMap()

    @Input
    var fileRenames: Map<String, String> = emptyMap()

    @Input
    var emoticons: Map<String, Int> = emptyMap()

    @TaskAction
    fun run() = runBlocking(Dispatchers.IO) {
        Environment.init(project.properties)
        val projectOutput = project.file(COMPILED_DIR).also {
            if (Environment.fullCompile) {
                it.deleteRecursively()
            }
            it.mkdirs()
        }

        coroutineScope {
            launch { replaceStrings(projectOutput, replacements) }
            launch { replaceEmoticons(projectOutput, emoticons) }
        }

        if (!Environment.fullCompile) {
            exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)
            return@runBlocking
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

        coroutineScope {
            launch { compileImages(contentDir, compilerOutput) }
            launch { compileSounds(contentDir) }
            launch { compileMaterials(contentDir) }
        }

        renameFiles(compilerOutput, fileRenames)

        compilerOutput.copyRecursively(projectOutput)
        exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)

        if (Environment.fullCompile) {
            val vpkFile = "pak${vpkIndex.toString().padStart(2, '0')}_dir.vpk"
            val destination = File(Environment.dotaRoot, "$INSTALL_DIR/$vpkFile")
            File(projectOutput, OUTPUT_VPK).copyTo(destination, overwrite = true)
            ++vpkIndex
        }

        contentDir.deleteRecursively()
        compilerOutput.deleteRecursively()
    }

    companion object {
        private var vpkIndex = 1

        fun reset() {
            File(Environment.dotaRoot, INSTALL_DIR).deleteRecursively()
            vpkIndex = 1
        }
    }
}