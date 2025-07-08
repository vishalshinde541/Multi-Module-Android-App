package com.example.network.models.remote

import com.example.network.models.domain.Episode
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEpisode(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)


fun RemoteEpisode.toDomainEpisode(): Episode{
    return Episode(
        id = id,
        name = name,
        seasonNumber = episode.filter { it.isDigit() }.take(2).toInt(),
        episodeNumber =  episode.filter { it.isDigit() }.takeLast(2).toInt(),
        airDate = air_date,
        characterIdsInEpisode = characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1).toInt()
        }
    )


}