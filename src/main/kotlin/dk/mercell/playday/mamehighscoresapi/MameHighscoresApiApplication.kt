package dk.mercell.playday.mamehighscoresapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class MameHighscoresApiApplication

fun main(args: Array<String>) {
	runApplication<MameHighscoresApiApplication>(*args)
}
