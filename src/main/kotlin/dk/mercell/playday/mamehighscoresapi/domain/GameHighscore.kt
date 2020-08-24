package dk.mercell.playday.mamehighscoresapi.domain

import java.nio.file.attribute.FileTime

data class GameHighscore(val name: String, val highscores: List<HighscoreEntry>, var lastModifiedTime: FileTime?)