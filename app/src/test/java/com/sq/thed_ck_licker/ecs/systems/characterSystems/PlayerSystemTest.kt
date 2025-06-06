package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class PlayerSystemTest {

    var playerSystem: PlayerSystem by Delegates.notNull()
    var cardPullingSystem: CardPullingSystem by Delegates.notNull()

    var cardsSystem: CardsSystem by Delegates.notNull()

    var cardCreationSystem: CardCreationSystem by Delegates.notNull()

    var playerId: EntityId = 0
    val cardCount = mutableIntStateOf(0)


    @BeforeEach
    fun setUp() {
        cardsSystem = CardsSystem(
            multiSystem = MultiplierSystem(
                componentManager = ComponentManager.componentManager
            )
        )
        cardCreationSystem = CardCreationSystem(
            cardsSystem = cardsSystem,
            cardBuilder = CardBuilderSystem(
                componentManager = ComponentManager.componentManager
            )
        )

        cardPullingSystem = CardPullingSystem(
            cardsSystem = cardsSystem
        )

        playerSystem = PlayerSystem(cardCreationSystem)
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

        cardPullingSystem.pullNewCard(mutableIntStateOf(-1))

        assert(
            drawDeck.getDrawCardDeck().isEmpty()
        ) { "Draw deck is not empty, instead was: ${drawDeck.getDrawCardDeck()}" }
    }

    @Test
    fun `on activation card goes to discard deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        playerId add DrawDeckComponent(cards)
        playerId add DiscardDeckComponent(mutableListOf())

        val card = mutableIntStateOf(-1)
        cardPullingSystem.pullNewCard(card)
        cardsSystem.cardActivation(card, cardCount)

        val draw = playerId get DrawDeckComponent::class
        val discard = playerId get DiscardDeckComponent::class

        assert(draw.getDrawCardDeck().size == 1) { "Draw deck does not ha ve only one card, but has $draw" }
        assert(discard.getDiscardDeck().size == 1) { "Discard deck does not have only one card, but has $discard" }
        assert(card.intValue == -1) { "Hand card was not removed, but is ${card.intValue}" }
    }

    @Test
    fun `on empty draw, shuffle discard to draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        playerId add DrawDeckComponent(mutableListOf())
        playerId add DiscardDeckComponent(cards)
        playerId add MultiplierComponent()

        val card = mutableIntStateOf(-1)
        repeat(2) {
            cardPullingSystem.pullNewCard(card)
            cardsSystem.cardActivation(card, cardCount)
        }
        cardPullingSystem.pullNewCard(card)

        val drawDeck = playerId get DrawDeckComponent::class
        val discardDeck = playerId get DiscardDeckComponent::class

        assert(drawDeck.getDrawCardDeck().size == 1) { "Cards were not returned to draw deck, draw deck contains $drawDeck" }
        assert(
            discardDeck.getDiscardDeck().isEmpty()
        ) { "Discard deck is not empty, discard deck contains $discardDeck" }
    }
}
