package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.HistoryComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems_Factory
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem_Factory
import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class CardsSystemTest {
    var multiSystem by Delegates.notNull<MultiplierSystem>()
    var cardCreationHelperSystems by Delegates.notNull<CardCreationHelperSystems>()
    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()
    var cardManager by Delegates.notNull<CardsSystem>()
    var owner by Delegates.notNull<EntityId>()

    @BeforeEach
    fun setUp() {
        multiSystem = MultiplierSystem_Factory.newInstance(ComponentManager.componentManager)
        cardCreationHelperSystems = CardCreationHelperSystems_Factory.newInstance()
        cardCreationSystem = CardCreationSystem(
            cardCreationHelperSystems = cardCreationHelperSystems,
            cardBuilder = CardBuilderSystem_Factory.newInstance(ComponentManager.componentManager),
            gameNavigator = GameNavigator_Factory.newInstance()
        )
        val playerSystem = PlayerSystem_Factory.newInstance(cardCreationSystem)
        cardManager = CardsSystem_Factory.newInstance(playerSystem)
        owner = EntityManager.getPlayerID()
    }

    @Test
    fun pullRandomCardFromEntityDeck() {
    }

    @Test
    fun `Activate entity's point card`() {
        val latest = LatestCardComponent()
        owner add latest
        owner add DiscardDeckComponent()
        owner add ScoreComponent()
        owner add HistoryComponent(owner)
        owner add MultiplierComponent()
        multiSystem.addHistoryComponentOfItself(owner)
        val count = mutableIntStateOf(0)
        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
        latest.setLatestCard(card)
        cardManager.cardActivation(count)

        val score = owner get ScoreComponent::class
        assert(score.getScore() == 100) { "Score was not modified by the card" }
    }

    @Test
    fun `Activate players point card 10 times`() {
        val latest = LatestCardComponent()
        owner add latest
        val discardDeckComponent = DiscardDeckComponent()
        owner add discardDeckComponent
        owner add ScoreComponent()
        owner add HistoryComponent(owner)
        owner add MultiplierComponent()
        multiSystem.addHistoryComponentOfItself(owner)
        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
        val count = mutableIntStateOf(0)


        repeat(10) {
            latest.setLatestCard(card)
            cardManager.cardActivation(count)
            if (it < 10) {
            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
                discardDeckComponent.removeCards(listOf(cardFromDiscard))
            }
        }

        val score = owner get ScoreComponent::class
        val endScore = 1000
        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
        assert(latest.getLatestCard() == -1) { "Latest card should be -1 but was ${latest.getLatestCard()}" }
    }

    @Test
    fun `Activate players point card 10 times with the new system`() {
        val latest = LatestCardComponent()
        owner add latest
        val discardDeckComponent = DiscardDeckComponent()
        owner add discardDeckComponent
        owner add ScoreComponent()
        owner add HistoryComponent(owner)
        owner add MultiplierComponent()
        multiSystem.addHistoryComponentOfItself(owner)
        val card = cardCreationSystem.addBasicScoreCards(1).first()
        val count = mutableIntStateOf(0)


        repeat(10) {
            latest.setLatestCard(card)
            cardManager.cardActivation(count)
            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
            discardDeckComponent.removeCards(listOf(cardFromDiscard))
        }

        val score = owner get ScoreComponent::class
        val endScore = 100
        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
        assert(latest.getLatestCard() == -1) { "Latest card should be -1 but was ${latest.getLatestCard()}" }
    }


    @Test
    fun `Activate players point card 7 times with multiplier`() {
        val latest = LatestCardComponent()
        owner add latest
        val discardDeckComponent = DiscardDeckComponent()
        owner add discardDeckComponent
        owner add ScoreComponent()
        owner add MultiplierComponent(2f)
        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
        multiSystem.addHistoryComponentOfItself(owner)

        val count = mutableIntStateOf(0)
        repeat(7) {
            latest.setLatestCard(card)
            cardManager.cardActivation(count)
            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
            discardDeckComponent.removeCards(listOf(cardFromDiscard))
        }

        val score = owner get ScoreComponent::class
        val endScore = 1400
        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
    }


    @Test
    fun `Activate players point card 7 times with temp multiplier`() {
        val latest = LatestCardComponent()
        owner add latest
        val discardDeckComponent = DiscardDeckComponent()
        owner add discardDeckComponent
        owner add ScoreComponent()
        owner add MultiplierComponent()

        cardCreationHelperSystems.addTemporaryMultiplierTo(
            targetEntityId = owner,
            health = 5f,
            multiplier = 6f
        )

        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
        multiSystem.addHistoryComponentOfItself(owner)

        val count = mutableIntStateOf(0)

        repeat(7) {
            latest.setLatestCard(card)
            cardManager.cardActivation(count)
            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
            discardDeckComponent.removeCards(listOf(cardFromDiscard))
        }

        val score = owner get ScoreComponent::class
        val endScore = 3200
        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
    }

//
//    @Test
//    fun `Activate players point card 7 times with new temp multiplier`() {
//        val latest = LatestCardComponent()
//        owner add latest
//        val discardDeckComponent = DiscardDeckComponent()
//        owner add discardDeckComponent
//        owner add ScoreComponent()
//        owner add MultiplierComponent()
//
//
//        val multiEntity = EntityManager.createNewEntity()
//        val healthComponent = HealthComponent(5f)
//        multiEntity add healthComponent
//        val effects = buildEffectComponent(healthComponent)
//        multiEntity add effects
//        effects.onSpecial.action(owner)
//
//        val card = cardCreationSystem.addBreakingDefaultCards(1).first()
//
//        val count = mutableIntStateOf(0)
//        multiSystem.addHistoryComponentOfItself(owner)
//        repeat(7) {
//            latest.setLatestCard(card)
//            cardManager.cardActivation(count)
//            val cardFromDiscard = discardDeckComponent.getDiscardDeck().first()
//            discardDeckComponent.removeCards(listOf(cardFromDiscard))
//        }
//
//        val score = owner get ScoreComponent::class
//        val endScore = 3200
//        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
//    }
//
//    fun buildEffectComponent(healthComponent: HealthComponent): EffectComponent {
//        val onSpecial = { targetId: Int ->
//            var targetMulti =
//                try {
//                    targetId get MultiplierComponent::class
//                } catch (_: Exception) {
//                    MultiplierComponent()
//                }
//            targetMulti.timesMultiplier(6f)
//            targetId add targetMulti
//        }
//        val describedOnSpecial = DescribedEffect(onSpecial) { "Add 6x multiplier" }
//
//
//        val onTurnStart = { _: Int -> healthComponent.damage(1f) }
//        val describedOnTurnStart = DescribedEffect(onTurnStart) { "Take 1 damage" }
//        val onDeath = { targetId: Int ->
//            var targetMulti = targetId get MultiplierComponent::class
//            targetMulti.removeMultiplier(6f)
//        }
//        val describedOnDeath = DescribedEffect(onDeath) { "Remove 6x multiplier" }
//
//        return EffectComponent(
//            onDeath = describedOnDeath,
//            onTurnStart = describedOnTurnStart,
//            onSpecial = describedOnSpecial
//        )
//    }
//
//
//    @Test
//    fun `First play multiplier card, then play point cards from players deck`() {
//        val latest = LatestCardComponent()
//        owner add latest
//        val discardDeckComponent = DiscardDeckComponent()
//        owner add discardDeckComponent
//        val score1 = ScoreComponent()
//        owner add score1
//        owner add MultiplierComponent()
//
//        val deck = mutableListOf<EntityId>()
//        val pointCards = cardCreationSystem.addBreakingDefaultCards(5)
//        deck.addAll(pointCards)
//
//        val multiCard = cardCreationSystem.addTempMultiplierTestCards(1).first()
//        multiSystem.addHistoryComponentOfItself(owner)
//        latest.setLatestCard(multiCard)
//
//        val count = mutableIntStateOf(0)
//        cardManager.cardActivation(count)
//        discardDeckComponent.removeCards(listOf(multiCard))
//
//        repeat(20) {
//            if (deck.isEmpty()) {
//                val hold = discardDeckComponent.getDiscardDeck()
//                deck.addAll(hold)
//                discardDeckComponent.removeCards(hold)
//            }
//            val next = deck.first()
//            deck.removeFirst()
//            latest.setLatestCard(next)
//            cardManager.cardActivation(count)
//        }
//
//        val score = owner get ScoreComponent::class
//        val endScore = 3800
//        assert(score.getScore() == endScore) { "Score should be $endScore but was ${score.getScore()}" }
//    }
}