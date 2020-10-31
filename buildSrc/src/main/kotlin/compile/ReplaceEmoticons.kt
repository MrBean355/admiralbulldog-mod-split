package compile

import service.GitHubService
import java.io.File

private const val EMOTICONS_FILE = "emoticons.txt"

fun replaceEmoticons(compiledDir: File, emoticons: Map<String, Int>) {
    if (emoticons.isEmpty()) {
        return
    }
    val emoticonsDirectory = File(compiledDir, "scripts").also {
        it.mkdirs()
    }
    downloadEmoticonsFile(emoticonsDirectory)
    replaceInFile(File(emoticonsDirectory, EMOTICONS_FILE), emoticons)
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