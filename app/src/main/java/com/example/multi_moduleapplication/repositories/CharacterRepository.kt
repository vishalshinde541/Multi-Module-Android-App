package com.example.multi_moduleapplication.repositories

import com.example.network.ApiResponse
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.network.models.domain.CharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacterByPage(page: Int): ApiResponse<CharacterPage>{
        return ktorClient.getCharacterByPage(pageNumber = page)
    }

    suspend fun fetchCharacter(characterId: Int): ApiResponse<Character> {
        return ktorClient.getCharacter(characterId)
    }
}