package dk.mercell.playday.mamehighscoresapi.domain

data class GameHighscore(val name: String, val romName: String, val highscores: List<HighscoreEntry>)