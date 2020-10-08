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
        appendln("<root>")
        appendln("  <Panel>")
        allFiles.forEach {
            check(it.endsWith(SUPPORTED_FILE_TYPE)) { "Unsupported file type: $it" }
            appendln("    <Image src=\"file://{images}/$it\" />")
        }
        appendln("  </Panel>")
        appendln("</root>")
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