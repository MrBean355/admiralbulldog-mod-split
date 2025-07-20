package compile

import service.GitHubService
import java.io.File

private const val TARGET_DIRECTORY = "resource/localization"
private val TARGET_FILES = arrayOf("abilities_english.txt", "dota_english.txt", "hero_chat_wheel_english.txt")

fun replaceStrings(compiledDir: File, replacements: Map<String, String>) {
    if (replacements.isEmpty()) {
        return
    }
    val stringsDirectory = File(compiledDir, TARGET_DIRECTORY).also {
        it.deleteRecursively()
        it.mkdirs()
    }
    val mappings = loadReplacements(replacements)
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
const val SINGLE_REPLACE = '@'
private const val COMMENT = '#'

private class Mappings {
    val contains = mutableMapOf<String, String>()
    val single = mutableMapOf<String, String>()
}

private fun loadReplacements(combine: Map<String, String>): Mappings {
    val mappings = Mappings()

    combine.forEach {
        val key = it.key
        when {
            key.startsWith(SINGLE_REPLACE) -> mappings.single += key.drop(1) to it.value
            else -> mappings.contains += key to it.value
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
            output.appendLine(line)
        } else {
            val key = match.groupValues[1]
            val oldValue = match.groupValues[2]
            var newValue = oldValue
            when (key) {
                in mappings.single -> newValue = mappings.single.getValue(key)
                else -> {
                    mappings.contains.forEach { (k, v) ->
                        newValue = newValue.replace(Regex("""\b${Regex.escape(k)}\b"""), v)
                    }
                }
            }
            output.appendLine(line.replace("\"$oldValue\"", "\"$newValue\""))
        }
    }
    input.writeText(output.toString())
}