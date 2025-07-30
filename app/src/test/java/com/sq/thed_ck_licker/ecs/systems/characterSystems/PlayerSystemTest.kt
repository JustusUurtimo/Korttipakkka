package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.dataStores.SettingsRepository
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem_Factory
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


    var ownerId: EntityId = 0

    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()

    var settings by Delegates.notNull<SettingsRepository>()

    @BeforeEach
    fun setUp() {
        cardCreationSystem = CardCreationSystem(
            gameNavigator = GameNavigator_Factory.newInstance()
        )

        playerSystem = PlayerSystem(cardCreationSystem, settings)

        cardsSystem = CardsSystem_Factory.newInstance(playerSystem)

        cardPullingSystem = CardPullingSystem(
            cardsSystem = cardsSystem, playerSystem = playerSystem
        )
        ownerId = EntityManager.getPlayerID()
    }

    @Test
    fun `player has draw deck`() {
        playerSystem.initPlayer()
        val drawDeck = assertDoesNotThrow { ownerId get DrawDeckComponent::class }
        assertNotNull(drawDeck) { "Player has no draw deck" }

    }

    @Test
    fun `player has discard deck`() {
        playerSystem.initPlayer()
        val discardDeck = assertDoesNotThrow { ownerId get DiscardDeckComponent::class }
        assertNotNull(discardDeck) { "Player has no discard deck" }
    }

    @Test
    fun `draw removes the card from the draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(1) as MutableList<Int>
        val drawDeck = DrawDeckComponent(cards)
        ownerId add drawDeck
        val latest = LatestCardComponent()
        ownerId add latest
        ownerId add DiscardDeckComponent(mutableListOf())

        cardPullingSystem.pullNewCard(ownerId)

        assert(
            drawDeck.getDrawCardDeck().isEmpty()
        ) { "Draw deck is not empty, instead was: ${drawDeck.getDrawCardDeck()}" }
    }

    @Test
    fun `on activation card goes to discard deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        ownerId add DrawDeckComponent(cards)
        ownerId add DiscardDeckComponent(mutableListOf())
        val latest = LatestCardComponent()
        ownerId add latest
        var cardCount = mutableIntStateOf(0)
//        ownerId add HistoryComponent(ownerId)

        cardPullingSystem.pullNewCard(ownerId)
        cardsSystem.cardActivation(cardCount)

        val draw = ownerId get DrawDeckComponent::class
        val discard = ownerId get DiscardDeckComponent::class

        assert(draw.getDrawCardDeck().size == 1) { "Draw deck does not ha ve only one card, but has $draw" }
        assert(discard.getDiscardDeck().size == 1) { "Discard deck does not have only one card, but has $discard" }
        assert((ownerId get LatestCardComponent::class).getLatestCard() == -1) { "Hand card was not removed, but is ${latest.getLatestCard()}" }
    }

    @Test
    fun `on empty draw, shuffle discard to draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        ownerId add DrawDeckComponent(mutableListOf())
        ownerId add DiscardDeckComponent(cards)
        ownerId add MultiplierComponent()
        val latest = LatestCardComponent()
        ownerId add latest

        var cardCount = mutableIntStateOf(0)

        repeat(2) {
            ownerId add latest
            cardPullingSystem.pullNewCard(ownerId)
            cardsSystem.cardActivation(cardCount)
        }
        cardPullingSystem.pullNewCard(ownerId)

        val drawDeck = ownerId get DrawDeckComponent::class
        val discardDeck = ownerId get DiscardDeckComponent::class

        assert(drawDeck.getDrawCardDeck().size == 1) { "Cards were not returned to draw deck, draw deck contains $drawDeck" }
        assert(
            discardDeck.getDiscardDeck().isEmpty()
        ) { "Discard deck is not empty, discard deck contains $discardDeck" }
    }
}
