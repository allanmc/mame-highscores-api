package dk.mercell.playday.mamehighscoresapi.controller

import dk.mercell.playday.mamehighscoresapi.domain.ConfigData
import dk.mercell.playday.mamehighscoresapi.service.GameRepository
import dk.mercell.playday.mamehighscoresapi.service.PlayerRepository
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
@RequestMapping("admin")
class AdminController(
        private val playerRepository: PlayerRepository,
        private val gameRepository: GameRepository
) {
    @GetMapping
    fun index(): ConfigData {
        return ConfigData(gameRepository.findAll(), playerRepository.findAll());
    }
    @PostMapping
    fun add(@RequestBody configData: ConfigData) {
        playerRepository.saveAll(configData.players)
        gameRepository.saveAll(configData.games)
    }
}
