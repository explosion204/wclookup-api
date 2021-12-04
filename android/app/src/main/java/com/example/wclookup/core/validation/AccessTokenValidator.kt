package com.example.wclookup.core.validation

import com.google.gson.Gson
import java.util.*


class AccessTokenValidator {
    companion object {
        private const val DOT = "."

        fun validateExpiryTime(accessToken: String): Boolean {
            return try {
                val chunks: List<String> = accessToken.split(DOT)
                val decoder: Base64.Decoder = Base64.getDecoder()
                val payload = String(decoder.decode(chunks[1]))

                val gson = Gson()
                val payloadObject: TokenPayload = gson.fromJson(payload, TokenPayload::class.java)
                val expiryTime: Long? = payloadObject.exp.toLongOrNull()
                val currentTime: Long = System.currentTimeMillis() / 1000L

                if (expiryTime != null) currentTime < expiryTime else false
            } catch (e: Exception) {
                // TODO: this exception is not enough specific
                false
            }
        }
    }

    private inner class TokenPayload(
        val sub: String,
        val name: String,
        val iat: String,
        val exp: String
    )
}