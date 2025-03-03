package com.example.meditationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meditationapp.ui.screens.HomeScreen
import com.example.meditationapp.ui.screens.FavoritesScreen
import com.example.meditationapp.ui.theme.MeditationAppTheme
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import com.example.meditationapp.ui.screens.Meditation
import kotlinx.coroutines.DisposableHandle

sealed class Screen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object Home : Screen(
        route = "home",
        icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
        label = "Главная"
    )
    object Favorites : Screen(
        route = "favorites",
        icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
        label = "Избранное"
    )
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeditationAppTheme {
                val navController = rememberNavController()
                val screens = listOf(Screen.Home, Screen.Favorites)
                var selectedScreen by remember { mutableStateOf(0) }
                
                var meditations by remember {
                    mutableStateOf(
                        listOf(
                            Meditation(1, "Утренняя медитация", "10 мин", "Начните свой день с позитивной энергией", R.raw.morning),
                            Meditation(2, "Медитация для сна", "15 мин", "Расслабляющая практика перед сном", R.raw.sleep),
                            Meditation(3, "Дыхательные практики", "8 мин", "Техники глубокого дыхания для снятия стресса", R.raw.breathing)
                        )
                    )
                }
                
                var currentPlayingMeditation by remember { mutableStateOf<Meditation?>(null) }
                var isPlaying by remember { mutableStateOf(false) }

                val exoPlayer = remember {
                    ExoPlayer.Builder(this).build()
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            screens.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    icon = screen.icon,
                                    label = { Text(screen.label) },
                                    selected = selectedScreen == index,
                                    onClick = {
                                        selectedScreen = index
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                meditations = meditations,
                                currentPlayingMeditation = currentPlayingMeditation,
                                isPlaying = isPlaying,
                                onPlayPause = { meditation ->
                                    if (currentPlayingMeditation?.id == meditation.id) {
                                        if (isPlaying) {
                                            exoPlayer.pause()
                                        } else {
                                            exoPlayer.play()
                                        }
                                        isPlaying = !isPlaying
                                    } else {
                                        currentPlayingMeditation = meditation
                                        val mediaItem = MediaItem.fromUri("android.resource://${packageName}/${meditation.audioResId}")
                                        exoPlayer.setMediaItem(mediaItem)
                                        exoPlayer.prepare()
                                        exoPlayer.play()
                                        isPlaying = true
                                    }
                                },
                                onToggleFavorite = { meditation ->
                                    meditations = meditations.map {
                                        if (it.id == meditation.id) {
                                            it.copy(isFavorite = !it.isFavorite)
                                        } else {
                                            it
                                        }
                                    }
                                }
                            )
                        }
                        composable(Screen.Favorites.route) {
                            FavoritesScreen(
                                meditations = meditations,
                                currentPlayingMeditation = currentPlayingMeditation,
                                isPlaying = isPlaying,
                                onPlayPause = { meditation ->
                                    if (currentPlayingMeditation?.id == meditation.id) {
                                        if (isPlaying) {
                                            exoPlayer.pause()
                                        } else {
                                            exoPlayer.play()
                                        }
                                        isPlaying = !isPlaying
                                    } else {
                                        currentPlayingMeditation = meditation
                                        val mediaItem = MediaItem.fromUri("android.resource://${packageName}/${meditation.audioResId}")
                                        exoPlayer.setMediaItem(mediaItem)
                                        exoPlayer.prepare()
                                        exoPlayer.play()
                                        isPlaying = true
                                    }
                                },
                                onToggleFavorite = { meditation ->
                                    meditations = meditations.map {
                                        if (it.id == meditation.id) {
                                            it.copy(isFavorite = !it.isFavorite)
                                        } else {
                                            it
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 