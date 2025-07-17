package com.example.multi_moduleapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_moduleapplication.components.common.DataPoint
import com.example.multi_moduleapplication.repositories.CharacterRepository
import com.example.multi_moduleapplication.screens.CharacterDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotEmpty

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _internalStateFlow = MutableStateFlow<CharacterDetailsViewState>(
        value = CharacterDetailsViewState.Loading
    )

    val stateFlow = _internalStateFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) {
        viewModelScope.launch {
            _internalStateFlow.update { return@update CharacterDetailsViewState.Loading }
            characterRepository.fetchCharacter(characterId).onSuccess { character ->
                val dataPoint = buildList {
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeIds.size.toString()))
                }

                _internalStateFlow.update {
                    return@update CharacterDetailsViewState.Success(
                        character = character,
                        characterDataPoints = dataPoint
                    )
                }

            }.onFailure { exception ->
                _internalStateFlow.update {
                    return@update CharacterDetailsViewState.Error(
                        message = exception.message ?: "Unknown Error occurred"
                    )
                }
            }
        }
    }
}