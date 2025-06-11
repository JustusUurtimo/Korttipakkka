package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem_Factory
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class PlayerSystemTest {

    var playerSystem: PlayerSystem by Delegates.notNull()
    var cardPullingSystem: CardPullingSystem by Delegates.notNull()

    var cardsSystem: CardsSystem by Delegates.notNull()


    var playerId: EntityId = 0
    val cardCount = mutableIntStateOf(0)

    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()

    @BeforeEach
    fun setUp() {
        cardCreationSystem = CardCreationSystem(
            cardCreationHelperSystems = CardCreationHelperSystems_Factory.newInstance(),
            cardBuilder = CardBuilderSystem_Factory.newInstance(ComponentManager.componentManager),
            gameNavigator = GameNavigator_Factory.newInstance()
        )

        playerSystem = PlayerSystem(cardCreationSystem)

        cardsSystem = CardsSystem_Factory.newInstance(
            MultiplierSystem(ComponentManager.componentManager), playerSystem
        )

        cardPullingSystem = CardPullingSystem(
            cardsSystem = cardsSystem, playerSystem = playerSystem
        )
        playerId = EntityManager.getPlayerID()
    }

    @Test
    fun `player has draw deck`() {
        playerSystem.initPlayer()
        val drawDeck = assertDoesNotThrow { playerId get DrawDeckComponent::class }
        assertNotNull(drawDeck) { "Player has no draw deck" }

    }

    @Test
    fun `player has discard deck`() {
        playerSystem.initPlayer()
        val discardDeck = assertDoesNotThrow { playerId get DiscardDeckComponent::class }
        assertNotNull(discardDeck) { "Player has no discard deck" }
    }

    @Test
    fun `draw removes the card from the draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(1) as MutableList<Int>
        val drawDeck = DrawDeckComponent(cards)
        playerId add drawDeck

        cardPullingSystem.pullNewCard(0)

        assert(
            drawDeck.getDrawCardDeck().isEmpty()
        ) { "Draw deck is not empty, instead was: ${drawDeck.getDrawCardDeck()}" }
    }

    @Test
    fun `on activation card goes to discard deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        playerId add DrawDeckComponent(cards)
        playerId add DiscardDeckComponent(mutableListOf())

        val latest = LatestCardComponent(-1)
        playerId add latest
        cardPullingSystem.pullNewCard(latest.getLatestCard())
        cardsSystem.cardActivation(cardCount)

        val draw = playerId get DrawDeckComponent::class
        val discard = playerId get DiscardDeckComponent::class

        assert(draw.getDrawCardDeck().size == 1) { "Draw deck does not ha ve only one card, but has $draw" }
        assert(discard.getDiscardDeck().size == 1) { "Discard deck does not have only one card, but has $discard" }
        assert((playerId get LatestCardComponent::class).getLatestCard() == -1) { "Hand card was not removed, but is ${latest.getLatestCard()}" }
    }

    @Test
    fun `on empty draw, shuffle discard to draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        playerId add DrawDeckComponent(mutableListOf())
        playerId add DiscardDeckComponent(cards)
        playerId add MultiplierComponent()
        val latest = LatestCardComponent(-1)
        playerId add latest

        repeat(2) {
            playerId add latest
            cardPullingSystem.pullNewCard(latest.getLatestCard())
            cardsSystem.cardActivation(cardCount)
        }
        cardPullingSystem.pullNewCard(latest.getLatestCard())

        val drawDeck = playerId get DrawDeckComponent::class
        val discardDeck = playerId get DiscardDeckComponent::class

        assert(drawDeck.getDrawCardDeck().size == 1) { "Cards were not returned to draw deck, draw deck contains $drawDeck" }
        assert(
            discardDeck.getDiscardDeck().isEmpty()
        ) { "Discard deck is not empty, discard deck contains $discardDeck" }
    }
}
