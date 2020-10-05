package dk.mercell.playday.mamehighscoresapi.domain

import javax.persistence.*

@Entity
data class Game(
        @Id
        val id: String,

        @Column(nullable = false)
        val enabled: Boolean
)