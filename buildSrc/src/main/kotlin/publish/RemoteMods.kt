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