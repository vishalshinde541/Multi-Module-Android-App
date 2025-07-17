package com.example.multi_moduleapplication.repositories

import com.example.network.ApiResponse
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacter(characterId: Int): ApiResponse<Character> {
        return ktorClient.getCharacter(characterId)
    }
}