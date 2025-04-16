package com.sq.thed_ck_licker.ecs.systems.characterSystems

import com.sq.thed_ck_licker.ecs.EntityManager
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.get
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class PlayerSystemTest {
    @Test
    fun `player has discard deck`() {
        val playerId = EntityManager.getPlayerID()
        PlayerSystem.instance.initPlayer()
        var discardDeck: DiscardDeckComponent? = null
        try {
            discardDeck = playerId get DiscardDeckComponent::class
        } catch (_: Exception) {
            fail("Player has no discard deck")
        }
        assertNotNull(discardDeck)
    }

}