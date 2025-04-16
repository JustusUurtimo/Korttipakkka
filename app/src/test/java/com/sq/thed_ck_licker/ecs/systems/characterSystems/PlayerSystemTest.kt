package com.sq.thed_ck_licker.ecs.systems.characterSystems

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.EntityManager
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.size
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.activationSystem
import com.sq.thed_ck_licker.ecs.systems.pullNewCardSystem
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem.Companion.instance as cardCreationSystem

class PlayerSystemTest {
    var playerId by Delegates.notNull<Int>()

    @BeforeEach
    fun setUp() {
        playerId = EntityManager.getPlayerID()
//        PlayerSystem.instance.initPlayer()

    }

    @Test
    fun `player has draw deck`() {
        PlayerSystem.instance.initPlayer()
        var drawDeck: DrawDeckComponent? = null
        try {
            drawDeck = playerId get DrawDeckComponent::class
        } catch (e: Exception) {
            Log.e("PlayerSystemTest", "Player has no draw deck: ${e.message}")
            fail("Player has no draw deck")
        }
        assertNotNull(drawDeck)
    }

    @Test
    fun `player has discard deck`() {
        PlayerSystem.instance.initPlayer()
        var discardDeck: DiscardDeckComponent? = null
        try {
            discardDeck = playerId get DiscardDeckComponent::class
        } catch (e: Exception) {
            Log.e("PlayerSystemTest", "Player has no discard deck: ${e.message}")
            fail("Player has no discard deck")
        }
        assertNotNull(discardDeck)
    }


    @Test
    fun `draw removes the card from the draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(1) as MutableList<Int>
        val drawDeck = DrawDeckComponent(cards)
        playerId add drawDeck

        pullNewCardSystem(mutableIntStateOf(-1), mutableIntStateOf(-1)).invoke()

        assert(drawDeck.size() == 0) //{ "No cards removed on draw" }
    }

    @Test
    fun `on activation card goes to discard deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        val drawDeck = DrawDeckComponent(cards)
        playerId add drawDeck

        val discardDeck = DiscardDeckComponent()
        playerId add discardDeck
        val card = mutableIntStateOf(-1)
        pullNewCardSystem(card, mutableIntStateOf(-1)).invoke()

        activationSystem(card, mutableIntStateOf(-1)).invoke()

        assert(drawDeck.size() == 1)
        assert(discardDeck.size() == 1)
        assert(card.intValue == -1)
    }


}