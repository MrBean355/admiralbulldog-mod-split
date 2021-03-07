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

import java.io.File

private const val SUPPORTED_FILE_TYPE = ".png"
private const val MANIFEST_FILE = "manifest.xml"
private const val COMPILED_MANIFEST_FILE = "manifest.vxml_c"
private val IMAGES_PATH = "panorama" + File.separator + "images" + File.separator

fun compileImages(sourceDir: File, compiledDir: File) {
    val imagesRoot = File(sourceDir, IMAGES_PATH)
    val allFiles = mutableListOf<String>()
    imagesRoot.findAllFiles(allFiles)

    if (!imagesRoot.exists() || allFiles.isEmpty()) {
        return
    }

    val manifestFile = File(imagesRoot, MANIFEST_FILE)

    val xml = buildString {
        appendLine("<root>")
        appendLine("  <Panel>")
        allFiles.forEach {
            check(it.endsWith(SUPPORTED_FILE_TYPE)) { "Unsupported file type: $it" }
            appendLine("    <Image src=\"file://{images}/$it\" />")
        }
        appendLine("  </Panel>")
        appendLine("</root>")
    }

    manifestFile.writeText(xml)

    ResourceCompiler.compile(sourceDir, "panorama/images/*")
    File(compiledDir, "panorama/images/$COMPILED_MANIFEST_FILE").delete()
}

private fun File.findAllFiles(items: MutableList<String>) {
    if (isDirectory) {
        listFiles()?.forEach { it.findAllFiles(items) }
    } else {
        items += absolutePath
            .substringAfter(IMAGES_PATH)
            .replace(File.separatorChar, '/')
    }
}