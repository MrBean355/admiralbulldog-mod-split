/*
 * Copyright 2021 Michael Johnston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        output.appendLine(newLine)
    }

    if (toReplace.isNotEmpty()) {
        error("Replacements not made: $toReplace")
    }

    input.writeText(output.toString())
}