package com.example.multi_moduleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.multi_moduleapplication.ui.theme.MultimoduleApplicationTheme
import com.example.network.Character
import com.example.network.KtorClient
import com.example.network.TestFile
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    val ktorClient = KtorClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var character by remember { mutableStateOf<Character?>(null) }

            LaunchedEffect(Unit) {
                delay(3000)
                character =  ktorClient.getCharacter(1)
            }

            MultimoduleApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Greeting(
                            name = "Android",
                        )
                        TestFile()

                        Text(character?.name ?: "No Character")
                    }


                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MultimoduleApplicationTheme {
        Greeting("Android")
    }
}