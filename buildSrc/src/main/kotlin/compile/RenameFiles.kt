package compile

import java.io.File

fun renameFiles(compiledDir: File, fileRenames: Map<String, String>) {
    fileRenames.forEach { (old, new) ->
        File(compiledDir, old).renameTo(File(compiledDir, new))
    }
}
