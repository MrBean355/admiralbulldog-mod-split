package compile

import org.gradle.api.Project
import java.io.File

private const val RENAMES_FILE = "renames.txt"

fun Project.renameFiles(compiledDir: File) {
    val input = file(RENAMES_FILE)
    if (!input.exists()) {
        return
    }
    loadRenames(input).forEach { (old, new) ->
        File(compiledDir, old).renameTo(File(compiledDir, new))
    }
}

private fun loadRenames(input: File): Map<String, String> {
    return input.readLines()
            .map { it.substringBefore('#').trim() }
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line
                        .substringBefore('#')
                        .split('=')

                require(parts.size == 2) { "Invalid syntax: $line" }
                parts.first().trim() to parts[1].trim()
            }.toMap()
}
