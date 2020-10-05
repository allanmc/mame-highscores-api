package dk.mercell.playday.mamehighscoresapi.domain

import java.time.LocalDateTime

data class GameHighscore(val name: String, val highscores: List<HighscoreEntry>, var lastModifiedTime: LocalDateTime?)