package com.example.ligascore

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class MatchViewModel : ViewModel() {
    private val _matches = mutableStateListOf<Match>()
    val matches: List<Match> get() = _matches

    private val _error = mutableStateOf("")
    val error: String get() = _error.value

    fun fetchMatches(date: LocalDate, apiKey: String) {
        viewModelScope.launch {
            try {
                // Appel corrigé pour utiliser le bon endpoint
                val response = RetrofitClient.apiService.getLaLigaMatches(
                    dateFrom = date.toString(),
                    dateTo = date.toString(),
                    apiKey = apiKey
                )

                println("Réponse API - Code: ${response.code()}, Body: ${response.body()}")

                if (response.isSuccessful) {
                    _matches.clear()
                    response.body()?.matches?.let { matches ->
                        _matches.addAll(matches)
                        _error.value = ""
                    } ?: run {
                        _error.value = "Aucun match trouvé"
                    }
                } else {
                    _error.value = "Erreur API : ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Erreur réseau : ${e.message}"
            }
        }
    }
}
