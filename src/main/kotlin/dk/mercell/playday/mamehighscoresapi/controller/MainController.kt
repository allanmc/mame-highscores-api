package dk.mercell.playday.mamehighscoresapi.controller

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import dk.mercell.playday.mamehighscoresapi.domain.Greeting
import dk.mercell.playday.mamehighscoresapi.utils.HighscoreUtils
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class MainController {
    val counter = AtomicLong()

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            arrayListOf(Greeting("mslug", "TCL", 100000),Greeting("ddp2", "TCL", 325325))
            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/run")
    fun run() = this.getHighScores()

    fun getHighScores(): List<GameHighscore> {
        return arrayListOf(
                GameHighscore("1943", "1943", HighscoreUtils.readHighscores("./data/hi/1943.hi")),
                //GameHighscore("ddp3", "ddp3", HighscoreUtils.readHighscores("./data/nvram/ddp3/sram")),
                GameHighscore("Metal Slug X", "mslugx", HighscoreUtils.readHighscores("./data/nvram/mslugx/saveram")),
                GameHighscore("1942", "1942", HighscoreUtils.readHighscores("./data/hi/1942.hi"))
        )
    }

}
