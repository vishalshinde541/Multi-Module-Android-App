package com.example.multi_moduleapplication.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multi_moduleapplication.components.character.CharacterDetailsNamePlateComponent
import com.example.multi_moduleapplication.components.character.CharacterGridItem
import com.example.multi_moduleapplication.components.character.CharacterListItem
import com.example.multi_moduleapplication.components.common.CharacterImage
import com.example.multi_moduleapplication.components.common.DataPoint
import com.example.multi_moduleapplication.components.common.DataPointComponent
import com.example.multi_moduleapplication.components.common.LoadingState
import com.example.multi_moduleapplication.ui.theme.RickAction
import com.example.multi_moduleapplication.viewmodels.CharacterDetailsViewModel
import com.example.network.models.domain.Character


sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ) : CharacterDetailsViewState
}

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onEpisodeClicked: (Int) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.fetchCharacter(characterId)
    }

    val state by viewModel.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {

        when (val viewState = state) {
            is CharacterDetailsViewState.Loading -> {
                item { LoadingState() }
            }

            is CharacterDetailsViewState.Error -> {

            }

            is CharacterDetailsViewState.Success -> {

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CharacterGridItem(
                            modifier = Modifier.weight(1f),
                            character = viewState.character
                        ) { }
                        Spacer(modifier = Modifier.width(16.dp))
                        CharacterGridItem(
                            modifier = Modifier.weight(1f),
                            character = viewState.character
                        ) { }
                    }

                }

                repeat(10){
                    item{ Spacer(modifier = Modifier.height(16.dp))}
                    item{
                        CharacterListItem(
                            character = viewState.character,
                            characterDataPoints = viewState.characterDataPoints
                        ) { }
                    }
                }

                item {
                    CharacterDetailsNamePlateComponent(
                        name = viewState.character.name,
                        characterStatus = viewState.character.status
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                //Image
                item {
                    CharacterImage(imageUrl = viewState.character.imageUrl)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                //Data Point
                items(viewState.characterDataPoints) {
                    Spacer(modifier = Modifier.height(32.dp))
                    DataPointComponent(dataPoint = it)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                // Button
                item {
                    Text(
                        text = "View all episodes",
                        color = RickAction,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .border(
                                width = 1.dp,
                                color = RickAction,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onEpisodeClicked(characterId) }
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                }

                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        }


        //Name plate


    }


}