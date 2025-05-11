package com.example.ligascore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.clickable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF1976D2),
                    secondary = Color(0xFF90CAF9),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E),
                    onPrimary = Color.White,
                    onSecondary = Color.Black,
                    onBackground = Color.White,
                    onSurface = Color.White,
                )
            ) {
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onMatchClick = { matchId ->
                                navController.navigate("detail/$matchId")
                            }
                        )
                    }
                    composable("detail/{matchId}") { backStackEntry ->
                        val matchId = backStackEntry.arguments?.getString("matchId")
                        MatchDetailScreen(matchId = matchId)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    selectedDate: LocalDate,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit
) {
    TopAppBar(
        title = { Text("La Liga - ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}") },
        navigationIcon = {
            IconButton(onClick = onPreviousDay) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Jour précédent")
            }
        },
        actions = {
            IconButton(onClick = onNextDay) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Jour suivant")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMatchClick: (String) -> Unit,
    viewModel: MatchViewModel = viewModel()
) {
    val apiKey = "8bd381eeb7e74616b4ae1712d09bdbaf"
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(selectedDate) {
        viewModel.fetchMatches(selectedDate, apiKey)
    }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                selectedDate = selectedDate,
                onPreviousDay = { selectedDate = selectedDate.minusDays(1) },
                onNextDay = { selectedDate = selectedDate.plusDays(1) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (viewModel.error.isNotEmpty()) {
                Text(
                    text = viewModel.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (viewModel.matches.isEmpty() && viewModel.error.isEmpty()) {
                Text(
                    text = "Aucun match aujourd'hui",
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn {
                items(viewModel.matches) { match ->
                    MatchItem(match = match, onClick = { onMatchClick(match.id.toString()) })
                }
            }
        }
    }
}

@Composable
fun MatchItem(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF232A34)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    match.homeTeam.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    match.awayTeam.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val hour = try {
                    match.utcDate.substring(11, 16)
                } catch (e: Exception) {
                    ""
                }
                Text(hour, color = Color(0xFF90CAF9))
                match.score?.fullTime?.let {
                    Text(
                        "${it.home ?: "-"} - ${it.away ?: "-"}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF1976D2)
                    )
                } ?: Text(
                    "VS",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF1976D2)
                )
            }
        }
    }
}

@Composable
fun MatchDetailScreen(matchId: String?) {
    // Implémentez l'écran de détail ici
    Text(
        text = "Détails du match ID: $matchId",
        modifier = Modifier.padding(16.dp),
        color = Color.White
    )
}
