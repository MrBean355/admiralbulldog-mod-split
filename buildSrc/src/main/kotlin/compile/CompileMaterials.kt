package compile

import java.io.File

private const val SOURCE_DIRECTORY = "materials"

fun compileMaterials(sourceDir: File) {
    if (!File(sourceDir, SOURCE_DIRECTORY).exists()) {
        return
    }
    ResourceCompiler.compile(sourceDir, "$SOURCE_DIRECTORY/*")
}