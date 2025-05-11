package com.example.ligascore

data class MatchResponse(
    val matches: List<Match>
)

data class Match(
    val id: Long, // Ajoute l'identifiant du match
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score?,
    val utcDate: String
)

data class Team(val name: String)

data class Score(val fullTime: FullTime)

data class FullTime(val home: Int?, val away: Int?)
