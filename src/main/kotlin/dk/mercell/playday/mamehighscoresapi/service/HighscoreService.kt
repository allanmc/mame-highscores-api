package dk.mercell.playday.mamehighscoresapi.service

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes


@Service
class HighscoreService(
        private val hi2TxtService: Hi2TxtService
) {

    fun getAllHighScores(): List<GameHighscore> {

        val gameHighScores = arrayListOf<GameHighscore>()

        gameHighScores.addAll(addGamesFromFolder("./data/hi/", false))
        gameHighScores.addAll(addGamesFromFolder("./data/nvram/", true))

        return gameHighScores
    }

    fun addGamesFromFolder(path: String, useParentName: Boolean): List<GameHighscore> {
        val gameHighScores = arrayListOf<GameHighscore>()
        File(path).walk().maxDepth(2).forEach {
            if (it.isFile) {
                println(it.absolutePath)
                val attr: BasicFileAttributes = Files.readAttributes(it.toPath(), BasicFileAttributes::class.java)
                val highscores = hi2TxtService.readHighscores(it, attr.lastModifiedTime())
                if (!highscores.isEmpty()) {
                    val name = if (useParentName) it.parentFile.name else it.name
                    gameHighScores.add(GameHighscore(name, name, highscores))
                }
            }
        }
        return gameHighScores
    }


}