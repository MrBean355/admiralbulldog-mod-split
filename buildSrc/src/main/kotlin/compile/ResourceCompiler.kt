package compile

import util.Environment
import util.exec
import java.io.File

object ResourceCompiler {
    private val exe = File(Environment.dotaRoot, "game/bin/win64/resourcecompiler.exe").absolutePath

    fun compile(workingDir: File, path: String) {
        exec("$exe -r -i \"$path\"", workingDir)
    }
}