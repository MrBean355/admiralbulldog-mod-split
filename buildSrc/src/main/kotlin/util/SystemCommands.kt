package util

import java.io.File
import java.util.concurrent.TimeUnit.MINUTES

fun exec(command: String, workingDir: File? = null) {
    println("Exec: $command")

    val process = ProcessBuilder(command.split("\\s".toRegex()))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectErrorStream(true)
            .directory(workingDir)
            .start()

    val output = process.inputStream.bufferedReader().readText().trim()
    process.waitFor(15, MINUTES)
    if (output.isNotEmpty()) {
        println(output)
    }
    check(process.exitValue() == 0) {
        "Unexpected exit code '${process.exitValue()}' for '$command'."
    }
}