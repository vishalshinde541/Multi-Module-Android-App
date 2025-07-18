package com.example.multi_moduleapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_moduleapplication.components.common.CharacterImage
import com.example.multi_moduleapplication.components.common.CharacterNameComponent
import com.example.multi_moduleapplication.components.common.DataPoint
import com.example.multi_moduleapplication.components.common.DataPointComponent
import com.example.multi_moduleapplication.components.common.LoadingState
import com.example.multi_moduleapplication.components.common.SimpleToolbar
import com.example.multi_moduleapplication.components.episode.EpisodeRowComponent
import com.example.multi_moduleapplication.ui.theme.RickPrimary
import com.example.multi_moduleapplication.ui.theme.RickTextPrimary
import com.example.network.KtorClient
import com.example.network.models.domain.Character
import com.example.network.models.domain.Episode
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodesScreen(characterID: Int, ktorClient: KtorClient,
                            onBackClicked: () -> Unit) {
    var characterState by remember { mutableStateOf<Character?>(null) }
    var episodeState by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(Unit) {
        ktorClient.getCharacter(characterID)
            .onSuccess { character ->
                characterState = character
                launch {
                    ktorClient.getEpisodes(character.episodeIds)
                        .onSuccess { episodes ->
                            episodeState = episodes
                        }.onFailure {

                        }
                }
            }.onFailure {
                // Handel the failure here
            }
    }

    characterState?.let { character ->
        MainScreen(character = character, episodes = episodeState, onBackClicked = onBackClicked)
    } ?: LoadingState()
}

@Composable
fun MainScreen(character: Character, episodes: List<Episode>, onBackClicked: () -> Unit) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }
    Column {
        SimpleToolbar(title = "Character episodes", onBackAction = onBackClicked)
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { CharacterNameComponent(character.name) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                LazyRow {
                    episodeBySeasonMap.forEach { mapEntry ->
                        val title = "Season ${mapEntry.key}"
                        val description = "${mapEntry.value.size} ep"
                        item {
                            DataPointComponent(
                                dataPoint = DataPoint(
                                    title = title,
                                    description = description
                                )
                            )
                        }
                        item { Spacer(modifier = Modifier.width(32.dp)) }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { CharacterImage(character.imageUrl) }

            episodeBySeasonMap.forEach { mapEntry ->
                stickyHeader { SeasonHeader(seasonNumber = mapEntry.key) }
                items(mapEntry.value) { episode ->
                    EpisodeRowComponent(episode = episode)
                }
            }
        }
    }

}

@Composable
private fun SeasonHeader(seasonNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = RickPrimary)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Season $seasonNumber",
            color = RickTextPrimary,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = RickTextPrimary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 8.dp)
        )
    }

}