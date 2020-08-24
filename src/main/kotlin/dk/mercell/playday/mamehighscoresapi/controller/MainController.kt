package dk.mercell.playday.mamehighscoresapi.controller

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import dk.mercell.playday.mamehighscoresapi.service.HighscoreService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController(
        private val highscoreService: HighscoreService
) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/list")
    fun run() = this.getHighScores()

    fun getHighScores(): List<GameHighscore> {
        return highscoreService.getAllHighScores()
    }

}
