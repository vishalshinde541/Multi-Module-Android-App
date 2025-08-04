package com.example.multi_moduleapplication.repositories

import com.example.network.ApiResponse
import com.example.network.KtorClient
import com.example.network.models.domain.Episode
import javax.inject.Inject

class EpisodesRepository @Inject constructor(val ktorClient: KtorClient) {
    suspend fun fetchAllEpisodes(): ApiResponse<List<Episode>> = ktorClient.getAllEpisodes()
}