package dk.mercell.playday.mamehighscoresapi.controller

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import dk.mercell.playday.mamehighscoresapi.service.HighscoreService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class MainController(
        private val highscoreService: HighscoreService
) {

    @GetMapping("/list")
    fun run() = this.getHighScores()

    fun getHighScores(): List<GameHighscore> {
        val allHighScores = highscoreService.getAllHighScores()

        val result = arrayListOf<GameHighscore>()
        for (game in allHighScores) {
            val higscores = game.highscores.take(3)
            result.add(GameHighscore(game.name, higscores, game.lastModifiedTime))
        }

        return result
    }

}
