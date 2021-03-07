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

import compile.SINGLE_REPLACE
import org.gradle.api.Action

open class ModExtension {
    val allReplacements: MutableMap<String, String> = mutableMapOf()
    val allFileRenames: MutableMap<String, String> = mutableMapOf()
    val allEmoticons: MutableMap<String, Int> = mutableMapOf()

    fun stringReplacements(action: Action<Replacements>) {
        Replacements().also {
            action.execute(it)
            allReplacements += it.replacements
        }
    }

    fun fileRenames(action: Action<Renames>) {
        Renames().also {
            action.execute(it)
            allFileRenames += it.renames
        }
    }

    fun emoticons(action: Action<Emoticons>) {
        Emoticons().also {
            action.execute(it)
            allEmoticons += it.millisPerFrame
        }
    }
}

class Replacements {
    val replacements = mutableMapOf<String, String>()

    fun id(id: String, replacement: String) {
        replacements["$SINGLE_REPLACE$id"] = replacement
    }

    fun any(match: String, replacement: String) {
        replacements[match] = replacement
    }
}

class Renames {
    val renames = mutableMapOf<String, String>()

    fun add(from: String, to: String) {
        renames += from to to
    }
}

class Emoticons {
    val millisPerFrame = mutableMapOf<String, Int>()

    fun add(name: String, millisPerFrame: Int) {
        this.millisPerFrame += name to millisPerFrame
    }
}