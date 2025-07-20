package service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface GitHubService {

    @GET("https://raw.githubusercontent.com/dotabuff/d2vpkr/master/dota/resource/localization/{fileName}")
    @Streaming
    fun getStringsFile(@Path("fileName") fileName: String): Call<ResponseBody>

    @GET("https://raw.githubusercontent.com/dotabuff/d2vpkr/master/dota/scripts/emoticons.txt")
    @Streaming
    fun getEmoticonsFile(): Call<ResponseBody>

    companion object {
        val INSTANCE = Retrofit.Builder()
            .baseUrl("http://unused")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<GitHubService>()
    }
}
