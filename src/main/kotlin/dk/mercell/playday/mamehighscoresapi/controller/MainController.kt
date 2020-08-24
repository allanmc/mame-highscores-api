package dk.mercell.playday.mamehighscoresapi.controller

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
}
