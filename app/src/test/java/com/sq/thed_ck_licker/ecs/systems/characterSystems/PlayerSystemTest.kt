package com.sq.thed_ck_licker.ecs.systems.characterSystems

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.size
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import java.util.Collections
import javax.inject.Inject
import kotlin.properties.Delegates
/*
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PlayerSystemTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }


    @Inject
    lateinit var playerSystem: PlayerSystem

    @Inject
    lateinit var cardPullingSystem: CardPullingSystem

    @Inject
    lateinit var cardsSystem: CardsSystem

    @Inject
    lateinit var cardCreationSystem: CardCreationSystem


    var playerId by Delegates.notNull<Int>()
    val cardCount = mutableIntStateOf(0)


    @BeforeEach
    fun setUp2() {
        playerId = EntityManager.getPlayerID()
    }

    @Test
    fun `player has draw deck`() {
        playerSystem.initPlayer()
        var drawDeck: DrawDeckComponent? = null
        try {
            //I have no idea why the 'get' thing would not work here..
            drawDeck = playerId get DrawDeckComponent::class
        } catch (e: IllegalStateException) {
            Log.e("PlayerSystemTest", "Player has no draw deck: ${e.message}")
            fail("Player has no draw deck")
        }
        assertNotNull(drawDeck)
    }

    @Test
    fun `player has discard deck`() {
        playerSystem.initPlayer()
        var discardDeck: DiscardDeckComponent? = null
        try {
            discardDeck = playerId get DiscardDeckComponent::class
        } catch (e: IllegalStateException) {
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

        cardPullingSystem.pullNewCard(mutableIntStateOf(-1))

        assert(drawDeck.size() == 0)
    }

    @Test
    fun `on activation card goes to discard deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        val drawDeck = DrawDeckComponent(cards)
        playerId add drawDeck

        val discardDeck = DiscardDeckComponent(mutableListOf<Int>())
        playerId add discardDeck
        val card = mutableIntStateOf(-1)
        cardPullingSystem.pullNewCard(card)

        cardsSystem.cardActivation(card, cardCount)

        assert((playerId get DrawDeckComponent::class).size() == 1)
        //it is beyond me why discardDeck.size() should not work here, but it does not so...
        assert((playerId get DiscardDeckComponent::class).size() == 1)
        assert(card.intValue == -1)
    }

    @Test
    fun `on empty draw  draw, shuffle discard to draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        val cards2 = MutableList<Int>(0) { it -> it }
        cards2.addAll(cards)
        Collections.copy<Int>(cards2, cards)
        val drawDeck = DrawDeckComponent(cards2)
        playerId add drawDeck

        val discardDeck = DiscardDeckComponent(mutableListOf<Int>())
        playerId add discardDeck
        val card = mutableIntStateOf(-1)
        repeat(2) {
            cardPullingSystem.pullNewCard(card)
            cardsSystem.cardActivation(card, cardCount)
        }
        cardPullingSystem.pullNewCard(card)

        val drawDeck2 = playerId get DrawDeckComponent::class
        val discardDeck2 = playerId get DiscardDeckComponent::class


        assert(drawDeck2.size() == 1)
        assert(discardDeck2.size() == 0)
        assert(cards.contains(card.intValue))
    }

}

 */