package com.example.multi_moduleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.multi_moduleapplication.screens.CharacterDetailsScreen
import com.example.multi_moduleapplication.ui.theme.MultimoduleApplicationTheme
import com.example.multi_moduleapplication.ui.theme.RickPrimary
import com.example.network.KtorClient

class MainActivity : ComponentActivity() {

    val ktorClient = KtorClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MultimoduleApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = RickPrimary
                    ) {
                        CharacterDetailsScreen(ktorClient, 125)
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
}