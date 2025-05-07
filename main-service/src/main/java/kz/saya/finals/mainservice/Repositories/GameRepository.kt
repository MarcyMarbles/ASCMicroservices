package kz.saya.finals.mainservice.Repositories

import kz.saya.finals.mainservice.Entities.Game
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GameRepository : JpaRepository<Game, UUID> {
    fun findAllByGenre(genre: String): MutableList<Game>
    fun findGameByName(gameName: String): Game
}