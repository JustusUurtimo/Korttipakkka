package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems_Factory
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class CardCreationSystemTest {
    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()
    var owner by Delegates.notNull<EntityId>()
    @BeforeEach
    fun setUp() {
        owner = EntityManager.getPlayerID()
        cardCreationSystem = CardCreationSystem(
            cardCreationHelperSystems = CardCreationHelperSystems_Factory.newInstance(),
            cardBuilder = CardBuilderSystem_Factory.newInstance(ComponentManager.componentManager),
            gameNavigator = GameNavigator_Factory.newInstance()
        )
    }

    @Test
    fun addBreakingDefaultCards() {
        val breakingCard = cardCreationSystem.addBreakingDefaultCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(breakingCard))
        val scoreComponent = ScoreComponent(100)
        owner add scoreComponent

//        val desc = (breakingCard get EffectComponent::class).onPlay.describe(owner)
//        (breakingCard get EffectComponent::class).onPlay.action(owner)

        val context = EffectContext(
            trigger = Trigger.OnPlay,
            source = breakingCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(scoreComponent.getScore() == 200) { "Score should be 200, but was ${scoreComponent.getScore()}" }
        val realDesc = "OnPlay:\nGain (100) points"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addTimeBoundTestCards() {
    }

    @Test
    fun addDamageCards() {
        val damageCard = cardCreationSystem.addDamageCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(damageCard))
        val healthComponent = HealthComponent(
            maxHealth = 1000f
        )
        owner add healthComponent

        val context = EffectContext(
            trigger = Trigger.OnPlay,
            source = damageCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 850f) { "Health should be 850f, but was ${healthComponent.getHealth()}" }
        val realDesc = "OnPlay:\nTake damage (150)"
        assert(desc == realDesc) { "Description should be\n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun `Activate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        owner add HealthComponent(10f)

        (trapCard get EffectComponent::class).onPlay.action(owner)
        val desc = (trapCard get EffectComponent::class).onPlay.describe(owner)

        assert((owner get HealthComponent::class).getHealth() == 5f) { "Health should be 5, but was ${(owner get HealthComponent::class).getHealth()}" }
        val realDesc = "Lose health based on activations (5.0 health)"
        assert(desc == realDesc) { "Description should be '$realDesc', but was '$desc'" }
    }

    @Test
    fun `Deactivate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val  scoreComponent = ScoreComponent(100)
        owner add scoreComponent

         (trapCard get EffectComponent::class).onDeactivate.action(owner)
        val desc = (trapCard get EffectComponent::class).onDeactivate.describe(owner)

        assert(scoreComponent.getScore() == 70) { "Score should be 70, but was ${owner get ScoreComponent::class}" }
        val realDesc = "Lose Score based on deactivations (30 score)"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }


    @Test
    fun `Activate and deactivate trap card multiple times`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val hpComponent = HealthComponent(300f)
        owner add hpComponent
        val scoreComponent = ScoreComponent(2000)
        owner add scoreComponent

        repeat(10) {
            (trapCard get EffectComponent::class).onDeactivate.action(owner)
            (trapCard get ActivationCounterComponent::class).deactivate()
            (trapCard get EffectComponent::class).onPlay.action(owner)
            (trapCard get ActivationCounterComponent::class).activate()
        }

        assert(hpComponent.getHealth() == 25f) { "Health should be 25, but was ${hpComponent.getHealth()}" }
        assert(scoreComponent.getScore() == 350) { "Score should be 350, but was ${scoreComponent.getScore()}" }
    }

    @Test
    fun addMaxHpTrapCards() {
    }

    @Test
    fun addBeerGogglesTestCards() {
    }


    @Test
    fun addBasicScoreCards() {
        val basicScoreCard = cardCreationSystem.addBasicScoreCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(basicScoreCard))
        val scoreComponent = ScoreComponent(0)
        owner add scoreComponent

        val context = EffectContext(
            trigger = Trigger.OnPlay,
            source = basicScoreCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(scoreComponent.getScore() == 10) { "Score should be 10, but was ${scoreComponent.getScore()}" }
        val realDesc = "OnPlay:\nGain (10) points"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }


    @Test
    fun addShuffleTestCards() {
    }

    @Test
    fun addShovelCards() {
    }

    @Test
    fun addHealingCards() {
        val healingCard = cardCreationSystem.addHealingCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(healingCard))
        val healthComponent = HealthComponent(
            health = 100f,
            maxHealth = 1000f
        )
        owner add healthComponent

        val context = EffectContext(
            trigger = Trigger.OnPlay,
            source = healingCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 140f) { "Health should be 140f, but was ${healthComponent.getHealth()}" }
        val realDesc = "OnPlay:\nHeal (40)"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addScoreGainerTestCards() {
    }

    @Test
    fun addDeactivationTestCards() {
    }

    @Test
    fun addTempMultiplierTestCards() {
    }

    @Test
    fun addMerchantCards() {
    }

}