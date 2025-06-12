package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem_Factory
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class CardsSystemTest {
    var cardCreationHelperSystems by Delegates.notNull<CardCreationHelperSystems>()
    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()
    var cardManager by Delegates.notNull<CardsSystem>()
    val playerId = EntityManager.getPlayerID()
    val cardCount = mutableIntStateOf(0)

    @BeforeEach
    fun setUp() {
        val multiSystem = MultiplierSystem_Factory.newInstance(ComponentManager.componentManager)
        cardCreationHelperSystems = CardCreationHelperSystems_Factory.newInstance()
        cardCreationSystem = CardCreationSystem(
            cardCreationHelperSystems = cardCreationHelperSystems,
            cardBuilder = CardBuilderSystem_Factory.newInstance(ComponentManager.componentManager),
            gameNavigator = GameNavigator_Factory.newInstance()
        )
        val playerSystem = PlayerSystem_Factory.newInstance(cardCreationSystem)
        cardManager = CardsSystem_Factory.newInstance(multiSystem, playerSystem)
        cardCount.intValue = 1
    }

    @Test
    fun pullRandomCardFromEntityDeck() {
    }

    @Test
    fun `Activate players point card`() {
        val latest = LatestCardComponent(-1)
        playerId add latest
        playerId add DiscardDeckComponent()
        playerId add ScoreComponent()
        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
        latest.setLatestCard(card)
        cardManager.cardActivation(cardCount)

        val score = playerId get ScoreComponent::class
        assert(score.getScore() == 100) { "Score was not modified by the card" }
    }

    @Test
    fun `Activate players point card 11 times`() {
        val latest = LatestCardComponent(-1)
        playerId add latest
        val discardDeckComponent = DiscardDeckComponent()
        playerId add discardDeckComponent
        playerId add ScoreComponent()
        val card = cardCreationSystem.addBreakingDefaultCards(1).first()

        repeat(11) {
            latest.setLatestCard(card)
            cardManager.cardActivation(cardCount)
            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
            discardDeckComponent.removeCards(listOf(cardFromDiscard))
        }

        val score = playerId get ScoreComponent::class
        val endScore = 1000
        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
        assert(latest.getLatestCard() == -1) { "Latest card should be -1 but was ${latest.getLatestCard()}" }
    }

}