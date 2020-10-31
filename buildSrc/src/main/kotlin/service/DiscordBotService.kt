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