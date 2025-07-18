package com.example.multi_moduleapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_moduleapplication.repositories.CharacterRepository
import com.example.multi_moduleapplication.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<HomeScreenViewState>(
        value = HomeScreenViewState.Loading
    )
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    fun fetchInitialPage(){
        viewModelScope.launch {
           val initialPage = characterRepository.fetchCharacterByPage(page = 1)
            initialPage.onSuccess { characterPage ->
                _viewState.update { return@update HomeScreenViewState.GridDisplay(
                    characters = characterPage.characters
                ) }
            }.onFailure {
                //TODO
            }
        }
    }

    fun fetchNextPage(){

    }


}