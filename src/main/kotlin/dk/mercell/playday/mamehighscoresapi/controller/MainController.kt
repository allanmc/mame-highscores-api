package dk.mercell.playday.mamehighscoresapi.controller

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import dk.mercell.playday.mamehighscoresapi.service.HighscoreService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class MainController(
        private val highscoreService: HighscoreService
) {

    @GetMapping("/list")
    fun run() = this.getHighScores()

    fun getHighScores(): List<GameHighscore> {
        val allHighScores = highscoreService.getAllHighScores()
        val sortedList = allHighScores.sortedWith(compareBy({ it.lastModifiedTime})).reversed()

        val result = arrayListOf<GameHighscore>()
        for (game in sortedList) {
            val higscores = game.highscores.take(3)
            result.add(GameHighscore(game.name, higscores, null))
        }

        return result
    }

}
