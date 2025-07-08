package com.example.multi_moduleapplication.components.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.multi_moduleapplication.components.common.CharacterNameComponent
import com.example.network.models.domain.CharacterStatus

@Composable
fun CharacterDetailsNamePlateComponent(name: String, characterStatus: CharacterStatus) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CharacterStatusComponent(characterStatus = characterStatus)
        CharacterNameComponent(name = name)
    }
}

@Preview
@Composable
private fun NamePlatePreviewAlive() {
    CharacterDetailsNamePlateComponent(name = "Rick Sanchez", CharacterStatus.Alive)
}

@Preview
@Composable
private fun NamePlatePreviewDead() {
    CharacterDetailsNamePlateComponent(name = "Rick Sanchez", CharacterStatus.Dead)
}

@Preview
@Composable
private fun NamePlatePreviewUnknown() {
    CharacterDetailsNamePlateComponent(name = "Rick Sanchez", CharacterStatus.Unknown)
}