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

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create("mod", ModExtension::class.java)

        target.tasks.register("buildMod", BuildModTask::class.java) {
            it.replacements = extension.allReplacements
            it.fileRenames = extension.allFileRenames
            it.emoticons = extension.allEmoticons
        }

        target.tasks.register("publishMod", PublishModTask::class.java)
    }
}

fun Project.dotaMod(configure: ModExtension.() -> Unit) =
    extensions.getByType(ModExtension::class.java).let(configure)