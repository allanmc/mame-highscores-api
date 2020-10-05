package dk.mercell.playday.mamehighscoresapi.service

import dk.mercell.playday.mamehighscoresapi.domain.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<Player, Long>