package com.example.meditationapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesScreen(
    meditations: List<Meditation>,
    onPlayPause: (Meditation) -> Unit,
    onToggleFavorite: (Meditation) -> Unit,
    currentPlayingMeditation: Meditation?,
    isPlaying: Boolean
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val favoriteMeditations = meditations.filter { it.isFavorite }
    val filteredMeditations = favoriteMeditations.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.description.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Избранные медитации",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoriteMeditations.isNotEmpty()) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }

        if (favoriteMeditations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "У вас пока нет избранных медитаций",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else if (filteredMeditations.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Медитации не найдены",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMeditations) { meditation ->
                    MeditationCard(
                        meditation = meditation,
                        isPlaying = isPlaying && currentPlayingMeditation?.id == meditation.id,
                        onPlayPause = { onPlayPause(meditation) },
                        onToggleFavorite = { onToggleFavorite(meditation) }
                    )
                }
            }
        }
    }
} 