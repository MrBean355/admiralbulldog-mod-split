package compile

import java.io.File

fun compileSounds(sourceDir: File) {
    if (!File(sourceDir, "sounds").exists()) {
        return
    }
    ResourceCompiler.compile(sourceDir, "sounds/*")
}