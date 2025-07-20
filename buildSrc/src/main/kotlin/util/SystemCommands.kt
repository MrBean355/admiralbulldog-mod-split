package util

import java.io.File
import java.util.concurrent.TimeUnit.MINUTES

fun exec(command: String, workingDir: File? = null) {
    log("Exec: $command")

    val process = ProcessBuilder(command.split("\\s".toRegex()))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectErrorStream(true)
        .directory(workingDir)
        .start()

    val output = process.inputStream.bufferedReader().readText().trim()
    process.waitFor(15, MINUTES)
    if (output.isNotEmpty()) {
        log(output)
    }
    check(process.exitValue() == 0) {
        "Unexpected exit code '${process.exitValue()}' for '$command'."
    }
}

private const val LOG_ENABLED = false

private fun log(text: String) {
    if (LOG_ENABLED) {
        println(text)
    }
}