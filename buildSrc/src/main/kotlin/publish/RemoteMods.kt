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

package publish

import service.DiscordBotService
import service.DotaMod

/** Fetch & update mod info on the server. */
object RemoteMods {

    fun getMod(key: String): DotaMod {
        val response = DiscordBotService.INSTANCE.getMod(key).execute()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            error("Failed to get mod: $response")
        }
        return body
    }

    fun updateModHash(key: String, newHash: String, size: Int) {
        val token = System.getenv("AUTH_TOKEN")
        require(!token.isNullOrBlank()) {
            "No token provided via AUTH_TOKEN"
        }
        val response = DiscordBotService.INSTANCE.patchMod(key, newHash, size, token).execute()
        if (!response.isSuccessful) {
            error("Failed to update mod hash: $response")
        }
    }
}