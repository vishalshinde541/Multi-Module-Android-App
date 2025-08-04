package com.example.multi_moduleapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_moduleapplication.repositories.EpisodesRepository
import com.example.multi_moduleapplication.screens.AllEpisodesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEpisodesViewModel @Inject constructor(
    val episodesRepository: EpisodesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllEpisodesUiState>(AllEpisodesUiState.Loading)
    val uiState = _uiState.asStateFlow()


    fun refreshAllEpisodes(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            episodesRepository.fetchAllEpisodes()
                .onSuccess { episodeList ->
                    _uiState.update {
                        AllEpisodesUiState.Success(
                            data = episodeList.groupBy { it.seasonNumber.toString() }.mapKeys {
                                "Season ${it.key}"
                            }
                        )
                    }
                }.onFailure {
                    _uiState.update { AllEpisodesUiState.Error }
                }
        }
    }

}