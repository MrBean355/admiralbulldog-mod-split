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

package service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscordBotService {

    @GET("mods/{key}")
    fun getMod(@Path("key") key: String): Call<DotaMod>

    @PATCH("mods/{key}")
    fun patchMod(
        @Path("key") key: String,
        @Query("hash") hash: String,
        @Query("size") size: Int,
        @Query("token") token: String
    ): Call<Unit>

    companion object {
        val INSTANCE = Retrofit.Builder()
            .baseUrl(hostUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<DiscordBotService>()

        private fun hostUrl(): String {
            return if (System.getenv("TARGET_ROONS_ENV") == "prod") {
                "http://prod.upmccxmkjx.us-east-2.elasticbeanstalk.com:8090/"
            } else {
                "http://localhost:8090/"
            }
        }
    }
}

data class DotaMod(
    val key: String,
    val hash: String
)