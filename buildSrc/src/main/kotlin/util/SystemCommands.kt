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