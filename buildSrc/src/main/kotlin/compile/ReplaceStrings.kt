package compile

import org.gradle.api.Project
import util.GitHubService
import java.io.File

private const val REPLACEMENTS_FILE = "replacements.txt"
private const val TARGET_DIRECTORY = "resource/localization"
private val TARGET_FILES = arrayOf("abilities_english.txt", "dota_english.txt", "hero_chat_wheel_english.txt")

fun Project.replaceStrings(compiledDir: File) {
    val input = file(REPLACEMENTS_FILE)
    if (!input.exists()) {
        return
    }
    val stringsDirectory = File(compiledDir, TARGET_DIRECTORY).also {
        it.deleteRecursively()
        it.mkdirs()
    }
    val mappings = loadReplacements(input)
    TARGET_FILES.forEach {
        downloadStringsFile(it, stringsDirectory)
        replaceInFile(File(stringsDirectory, it), mappings)
    }
}

private fun downloadStringsFile(fileName: String, destination: File): File {
    val response = GitHubService.INSTANCE.getStringsFile(fileName).execute()
    val bytes = response.body()?.bytes() ?: error("Null body received for $fileName")
    return File(destination, fileName).apply {
        writeBytes(bytes)
    }
}

/**
 * Only replace lines that match this pattern.
 *
 * Example:
 * 		"DOTA_Tooltip_Ability_item_hand_of_midas"			"Hand of Midas"    // optional comment which says stuff
 */
private val FILE_ENTRY_PATTERN = Regex("^\\s*\"(.*)\"\\s*\"(.*)\".*$")

// Symbols used in the mappings file:
private const val SEPARATOR = '='
private const val EXACT_REPLACE = '!'
private const val SINGLE_REPLACE = '@'
private const val COMMENT = '#'

private class Mappings {
    val contains = mutableMapOf<String, String>()
    val exact = mutableMapOf<String, String>()
    val single = mutableMapOf<String, String>()
}

private fun loadReplacements(replacements: File): Mappings {
    require(replacements.exists()) { "Replacements file doesn't exist: ${replacements.absolutePath}" }
    val mappings = Mappings()
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
                when {
                    key.startsWith(EXACT_REPLACE) -> mappings.exact += key.drop(1) to value
                    key.startsWith(SINGLE_REPLACE) -> mappings.single += key.drop(1) to value
                    else -> mappings.contains += key to value
                }
            }

    return mappings
}

private fun replaceInFile(input: File, mappings: Mappings) {
    require(input.exists()) { "Input file doesn't exist: ${input.absolutePath}" }
    val output = StringBuilder()
    input.forEachLine { line ->
        val match = FILE_ENTRY_PATTERN.matchEntire(line)
        if (match == null) {
            output.append(line + "\n")
        } else {
            val key = match.groupValues[1]
            val oldValue = match.groupValues[2]
            var newValue = oldValue
            when {
                key in mappings.single -> newValue = mappings.single[key]!!
                oldValue in mappings.exact -> newValue = mappings.exact[oldValue]!!
                else -> {
                    mappings.contains.forEach { (k, v) ->
                        newValue = newValue.replace(k, v)
                    }
                }
            }
            output.append(line.replace("\"$oldValue\"", "\"$newValue\"") + "\n")
        }
    }
    input.writeText(output.toString())
}