package com.example.network

import com.example.network.models.domain.Character
import com.example.network.models.domain.CharacterPage
import com.example.network.models.domain.Episode
import com.example.network.models.domain.EpisodePage
import com.example.network.models.remote.RemoteCharacter
import com.example.network.models.remote.RemoteCharacterPage
import com.example.network.models.remote.RemoteEpisode
import com.example.network.models.remote.RemoteEpisodePage
import com.example.network.models.remote.toDomainCharacter
import com.example.network.models.remote.toDomainCharacterPage
import com.example.network.models.remote.toDomainEpisode
import com.example.network.models.remote.toDomainEpisodePage
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

    private var characterCache = mutableMapOf<Int, Character>()

    suspend fun getCharacter(id: Int): ApiResponse<Character> {
        characterCache[id]?.let { return ApiResponse.Success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    suspend fun getCharacterByPage(pageNumber: Int): ApiResponse<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>()
                .toDomainCharacterPage()
        }
    }

    suspend fun getEpisode(episodeId: Int): ApiResponse<Episode> {
        return safeApiCall {
            client.get("episode/$episodeId")
                .body<RemoteEpisode>()
                .toDomainEpisode()
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiResponse<List<Episode>> {
        return if (episodeIds.size == 1) {
            getEpisode(episodeIds[0]).mapSuccess { episode ->
                listOf(episode)
            }
        } else {
            val commaSeparatedIds = episodeIds.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$commaSeparatedIds")
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    suspend fun getEpisodesByPage(pageIndex: Int): ApiResponse<EpisodePage> {
        return safeApiCall {
            client.get("episode") {
                url {
                    parameters.append("page", pageIndex.toString())
                }
            }
                .body<RemoteEpisodePage>()
                .toDomainEpisodePage()
        }
    }

//    suspend fun getAllEpisodes(): ApiResponse<List<Episode>> {
//        val data = mutableListOf<Episode>()
//        var exception: Exception? = null
//
//        getEpisodesByPage(pageIndex = 1).onSuccess { firstPage ->
//            val totalPageCount = firstPage.info.pages
//            data.addAll(firstPage.episodes)
//
//            repeat(totalPageCount - 1) { index ->
//                getEpisodesByPage(pageIndex = index + 2).onSuccess { nextPage ->
//                    data.addAll(nextPage.episodes)
//                }.onFailure { error ->
//                    exception = error
//                }
//                if (exception == null) {
//                    return@onSuccess
//                }
//            }
//
//        }.onFailure { error ->
//            exception = error
//        }
//
//        return exception?.let { ApiResponse.Failure(it) } ?: ApiResponse.Success(data)
//    }

    suspend fun getAllEpisodes(): ApiResponse<List<Episode>> {
        val data = mutableListOf<Episode>()

        // Get first page

        when (val firstPageResult = getEpisodesByPage(1)) {
            is ApiResponse.Failure -> {
                return ApiResponse.Failure(firstPageResult.exception)
            }

            is ApiResponse.Success -> {
                val totalPages = firstPageResult.data.info.pages
                data.addAll(firstPageResult.data.episodes)

                // Fetch remaining pages
                for (page in 2..totalPages) {
                    when (val result = getEpisodesByPage(page)) {
                        is ApiResponse.Failure -> {
                            return ApiResponse.Failure(result.exception)
                        }

                        is ApiResponse.Success -> {
                            data.addAll(result.data.episodes)
                        }
                    }
                }
            }
        }

        return ApiResponse.Success(data)
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

    fun <R> mapSuccess(transform: (T) -> (R)): ApiResponse<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(exception)
        }
    }

   suspend fun onSuccess(block: (T) -> Unit): ApiResponse<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiResponse<T> {
        if (this is Failure) block(exception)
        return this
    }
}
