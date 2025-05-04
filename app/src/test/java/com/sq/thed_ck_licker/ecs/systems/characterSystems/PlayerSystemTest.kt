package com.sq.thed_ck_licker.ecs.systems.characterSystems
/*
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.size
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import org.junit.jupiter.api.BeforeEach
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject
import kotlin.properties.Delegates


class PlayerSystemTest {

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
    fun setUp() {
        val testComponent = DaggerTestComponent.create()
        testComponent.inject(this)
        // Initialize PlayerSystem manually for unit tests
        playerSystem = PlayerSystem(cardCreationSystem)
        playerId = EntityManager.getPlayerID()
    }

    @Test
    fun `player has draw deck`() {
        playerSystem.initPlayer()
        val drawDeck = assertDoesNotThrow { playerId get DrawDeckComponent::class }
        assertNotNull(drawDeck)
    }

    @Test
    fun `player has discard deck`() {
        playerSystem.initPlayer()
        val discardDeck = assertDoesNotThrow { playerId get DiscardDeckComponent::class }
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
        playerId add DrawDeckComponent(cards)
        playerId add DiscardDeckComponent(mutableListOf())

        val card = mutableIntStateOf(-1)
        cardPullingSystem.pullNewCard(card)
        cardsSystem.cardActivation(card, cardCount)

        assert((playerId get DrawDeckComponent::class).size() == 1)
        assert((playerId get DiscardDeckComponent::class).size() == 1)
        assert(card.intValue == -1)
    }

    @Test
    fun `on empty draw, shuffle discard to draw deck`() {
        val cards = cardCreationSystem.addBasicScoreCards(2) as MutableList<Int>
        playerId add DrawDeckComponent(mutableListOf())
        playerId add DiscardDeckComponent(cards)

        val card = mutableIntStateOf(-1)
        cardPullingSystem.pullNewCard(card)

        val drawDeck = playerId get DrawDeckComponent::class
        val discardDeck = playerId get DiscardDeckComponent::class

        assert(drawDeck.size() == 2) // All cards moved to draw deck
        assert(discardDeck.size() == 0) // Discard deck is empty
    }
}
*/