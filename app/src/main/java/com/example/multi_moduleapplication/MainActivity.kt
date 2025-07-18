package com.example.multi_moduleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multi_moduleapplication.screens.CharacterDetailsScreen
import com.example.multi_moduleapplication.screens.CharacterEpisodesScreen
import com.example.multi_moduleapplication.ui.theme.MultimoduleApplicationTheme
import com.example.multi_moduleapplication.ui.theme.RickPrimary
import com.example.network.KtorClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient : KtorClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
        val navController = rememberNavController()

            MultimoduleApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = RickPrimary
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "character_details"
                        ) {
                            composable("character_details") {
                                CharacterDetailsScreen( 1) {
                                    navController.navigate("character_episodes/$it")
                                }
                            }
                            composable(
                                route = "character_episodes/{characterId}",
                                arguments = listOf(navArgument("characterId") {
                                    type =
                                        NavType.IntType
                                })
                            ) { backStackEntry ->
                                val characterId =
                                    backStackEntry.arguments?.getInt("characterId") ?: -1
                                CharacterEpisodesScreen(characterID = characterId, ktorClient = ktorClient)
                            }
                        }
//                        CharacterDetailsScreen(ktorClient, 173)
                    }

                }
            }
        }
    }
}

