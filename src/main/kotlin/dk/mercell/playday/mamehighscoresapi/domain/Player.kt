package dk.mercell.playday.mamehighscoresapi.domain

import javax.persistence.*

@Entity
data class Player(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        val name: String,

        val tags: String
)