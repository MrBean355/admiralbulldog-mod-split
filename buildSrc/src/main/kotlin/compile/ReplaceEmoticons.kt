package compile

import org.gradle.api.Project
import util.GitHubService
import java.io.File

private const val REPLACEMENTS_FILE = "emoticons.txt"
private const val EMOTICONS_FILE = "emoticons.txt"

fun Project.replaceEmoticons(compiledDir: File, emoticons: Map<String, Int>) {
    val input = file(REPLACEMENTS_FILE)
    if (!input.exists()) {
        return
    }
    val emoticonsDirectory = File(compiledDir, "scripts").also {
        it.mkdirs()
    }
    downloadEmoticonsFile(emoticonsDirectory)
    replaceInFile(File(emoticonsDirectory, EMOTICONS_FILE), loadReplacements(input, emoticons))
}

private fun downloadEmoticonsFile(destination: File): File {
    val response = GitHubService.INSTANCE.getEmoticonsFile().execute()
    val bytes = response.body()?.bytes() ?: error("Null body received for $EMOTICONS_FILE")
    return File(destination, EMOTICONS_FILE).apply {
        writeBytes(bytes)
    }
}

/**
 * Only replace lines that match this pattern.
 *
 * Example:
 * 		"image_name" "rage.png"
 */
private val FILE_ENTRY_PATTERN = Regex("^\\s+\"(.*)\"\\s+\"(.*)\"$")

// Symbols used in the mappings file:
private const val SEPARATOR = '='
private const val COMMENT = '#'

private fun loadReplacements(replacements: File, combine: Map<String, Int>): Map<String, Int> {
    require(replacements.exists()) { "Replacements file doesn't exist: ${replacements.absolutePath}" }
    val mappings = combine.toMutableMap()
    replacements.readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filter { !it.startsWith(COMMENT) }
            .forEach { line ->
                val parts = line
                        .substringBefore(COMMENT)
                        .split(SEPARATOR)

                require(parts.size == 2) { "Invalid syntax: $line" }
                val key = parts.first().trim()
                val value = parts[1].trim()
                mappings += key to value.toInt()
            }

    return mappings
}

private fun replaceInFile(input: File, mappings: Map<String, Int>) {
    require(input.exists()) { "Input file doesn't exist: ${input.absolutePath}" }
    val toReplace = mappings.keys.toMutableSet()
    val output = StringBuilder()
    var imageName = ""

    input.forEachLine { line ->
        val result = FILE_ENTRY_PATTERN.find(line)
        val key = result?.groups?.get(1)?.value
        val value = result?.groups?.get(2)?.value
        var newLine = line

        if (key != null && value != null) {
            when (key) {
                "image_name" -> imageName = value
                "ms_per_frame" -> {
                    if (imageName.isNotEmpty() && imageName in mappings) {
                        newLine = "\t\t\"ms_per_frame\" \"${mappings.getValue(imageName)}\""
                        toReplace -= imageName
                        imageName = ""
                    }
                }
            }
        }
        output.appendln(newLine)
    }

    if (toReplace.isNotEmpty()) {
        error("Replacements not made: $toReplace")
    }

    input.writeText(output.toString())
}