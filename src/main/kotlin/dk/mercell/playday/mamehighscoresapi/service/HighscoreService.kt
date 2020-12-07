package dk.mercell.playday.mamehighscoresapi.service

import dk.mercell.playday.mamehighscoresapi.domain.Game
import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneId


@Service
class HighscoreService(
        private val hi2TxtService: Hi2TxtService,
        private val gameRepository: GameRepository,
        private val playerRepository: PlayerRepository
) {

    fun getAllHighScores(): List<GameHighscore> {

        val gameHighScores = arrayListOf<GameHighscore>()

        gameHighScores.addAll(addGamesFromFolder("./data/hi/", false))
        gameHighScores.addAll(addGamesFromFolder("./data/nvram/", true))

        val dbGames = gameRepository.findAll()
        updateKnownGamesInDB(dbGames, gameHighScores)
        updatePlayers(gameHighScores)

        return filterDisabledGames(dbGames, gameHighScores)
            .sortedWith(compareBy { it.lastModifiedTime }).reversed()
            .distinctBy { it.name }
    }

    private fun updatePlayers(gameHighScores: List<GameHighscore>) {
        val players = playerRepository.findAll()
        val playerMap: HashMap<String, String> = hashMapOf()
        for (player in players) {
            val tags = player.tags.split(',')
            for (tag in tags) {
                playerMap.put(tag.toLowerCase(), player.name)
            }
        }
        for (gameHighScore in gameHighScores) {
            for (highscore in gameHighScore.highscores) {
                highscore.alias = playerMap.getOrDefault(highscore.name.toLowerCase().trim(), highscore.name)
            }
        }
    }

    private fun filterDisabledGames(dbGames: List<Game>, gameHighScores: List<GameHighscore>): List<GameHighscore> {
        val enabledGameNames = dbGames.filter { game -> game.enabled }.map { game -> game.id }
        return gameHighScores.filter { gameHighScore -> enabledGameNames.contains(gameHighScore.name) }
    }

    private fun updateKnownGamesInDB(dbGames: List<Game>, loadedGames: List<GameHighscore>) {
        val loadedGameNames = loadedGames.map { loadedGame -> loadedGame.name }
        val dbGameNames = dbGames.map { game -> game.id }
        val newGameNames = loadedGameNames
                .filter { s -> !dbGameNames.contains(s) }
                .distinct()
        for (newGameName in newGameNames) {
            gameRepository.save(Game(newGameName, false))
        }
    }

    fun addGamesFromFolder(path: String, useParentName: Boolean): List<GameHighscore> {
        val gameHighScores = arrayListOf<GameHighscore>()
        File(path).walk().maxDepth(2).forEach {
            if (it.isFile) {
                val attr: BasicFileAttributes = Files.readAttributes(it.toPath(), BasicFileAttributes::class.java)
                val highscores = hi2TxtService.readHighscores(it, attr.lastModifiedTime())
                if (!highscores.isEmpty()) {
                    val name = (if (useParentName) it.parentFile.name else it.name).replace(".hi", "")
                    gameHighScores.add(GameHighscore(name, highscores, LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault())))
                }
            }
        }
        return gameHighScores
    }


}