package com.example.multi_moduleapplication.components.character

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_moduleapplication.ui.theme.RickTextPrimary
import com.example.network.models.domain.CharacterStatus

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = characterStatus.color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = "Status: ${characterStatus.displayName}",
            fontSize = 20.sp,
            color = RickTextPrimary
        )
    }
}

@Preview
@Composable
private fun CharacterStatusComponentPreviewAlive() {
    CharacterStatusComponent(CharacterStatus.Alive)
}

@Preview
@Composable
private fun CharacterStatusComponentPreviewDead() {
CharacterStatusComponent(CharacterStatus.Dead)
}

@Preview
@Composable
private fun CharacterStatusComponentPreviewUnknown() {
CharacterStatusComponent(CharacterStatus.Unknown)
}