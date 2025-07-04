package com.example.network

import com.example.network.models.domain.Character
import com.example.network.models.remote.RemoteCharacter
import com.example.network.models.remote.toDomainCharacter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    val client = HttpClient(OkHttp) {
        defaultRequest { url("https://rickandmortyapi.com/api/") }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

    }

    suspend fun getCharacter(id: Int): ApiResponse<Character> {
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiResponse<T> {
        return try {
            ApiResponse.Success(data = apiCall())
        } catch (e: Exception) {
            ApiResponse.Failure(exception = e)
        }
    }
}

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Failure<T>(val exception: Exception) : ApiResponse<T>

    fun onSuccess(block: (T) -> Unit): ApiResponse<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiResponse<T> {
        if (this is Failure) block(exception)
        return this
    }
}
