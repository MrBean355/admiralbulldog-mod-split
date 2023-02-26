/*
 * Copyright 2022 Michael Johnston
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

import compile.compileImages
import compile.compileMaterials
import compile.compileSounds
import compile.renameFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import util.Environment
import util.exec
import java.io.File

private const val COMPILED_DIR = "compiled"
private const val OUTPUT_VPK = "pak01_dir.vpk"

open class BuildModTask : DefaultTask() {
    @Input
    var replacements: Map<String, String> = emptyMap()

    @Input
    var fileRenames: Map<String, String> = emptyMap()

    @Input
    var emoticons: Map<String, Int> = emptyMap()

    @TaskAction
    fun run() = runBlocking(Dispatchers.IO) {
        Environment.init(project.properties)
        val projectOutput = project.file(COMPILED_DIR).also {
            if (Environment.fullCompile) {
                it.deleteRecursively()
            }
            it.mkdirs()
        }

        coroutineScope {
            // Disabled until we can download the source files from somewhere.
            // launch { replaceStrings(projectOutput, replacements) }
            // launch { replaceEmoticons(projectOutput, emoticons) }
        }

        if (!Environment.fullCompile) {
            exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)
            return@runBlocking
        }

        val contentDir = File(Environment.dotaRoot, "content/dota_addons/${project.name}").also {
            it.deleteRecursively()
            it.mkdirs()
        }
        val compilerOutput = File(Environment.dotaRoot, "game/dota_addons/${project.name}").also {
            it.deleteRecursively()
            it.mkdirs()
        }

        project.projectDir.listFiles().orEmpty()
            .filter { it.isDirectory && it.name != COMPILED_DIR }
            .forEach { it.copyRecursively(File(contentDir, it.name)) }

        coroutineScope {
            launch { compileImages(contentDir, compilerOutput) }
            launch { compileSounds(contentDir) }
            launch { compileMaterials(contentDir) }
        }

        renameFiles(compilerOutput, fileRenames)

        compilerOutput.copyRecursively(projectOutput)
        exec("vpk -c . $OUTPUT_VPK", workingDir = projectOutput)

        if (Environment.fullCompile) {
            val destination = File(Environment.dotaRoot, "game/${project.name}/$OUTPUT_VPK")
            File(projectOutput, OUTPUT_VPK).copyTo(destination, overwrite = true)
        }

        contentDir.deleteRecursively()
        compilerOutput.deleteRecursively()
    }
}