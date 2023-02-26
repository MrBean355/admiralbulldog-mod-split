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

package util

import java.io.File

private const val PROPERTY_DOTA_ROOT = "dota.root"
private const val PROPERTY_FULL_COMPILE = "compiler.fullCompile"
private const val PROPERTY_SEND_MESSAGE = "telegram.sendMessage"

object Environment {
    var dotaRoot: File = File("")
        private set
    var fullCompile: Boolean = false
        private set
    var sendMessage: Boolean = false
        private set

    fun init(properties: Map<String, *>) {
        val dotaRootProperty = properties[PROPERTY_DOTA_ROOT] as? String ?: ""

        dotaRoot = File(dotaRootProperty)
        fullCompile = (properties[PROPERTY_FULL_COMPILE] as? String)?.toBoolean() ?: true
        sendMessage = (properties[PROPERTY_SEND_MESSAGE] as? String)?.toBoolean() ?: false
    }
}