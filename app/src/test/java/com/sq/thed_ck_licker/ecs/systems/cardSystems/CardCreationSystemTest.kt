package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
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
        owner = EntityManager.createNewEntity()
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

        val desc = (breakingCard get EffectComponent::class).onPlay(owner)

        assert(scoreComponent.getScore() == 200) { "Score should be 200, but was ${scoreComponent.getScore()}" }
        val realDesc = "Gain 100 points"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addTimeBoundTestCards() {
    }

    @Test
    fun addDamageCards() {
    }

    @Test
    fun `Activate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        owner add HealthComponent(10f)
        val desc = (trapCard get EffectComponent::class).onPlay(owner)

        assert((owner get HealthComponent::class).getHealth() == 5f) { "Health should be 5, but was ${owner get HealthComponent::class}" }
        val realDesc = "Lose health based on activations (5.0 health)"
        assert(desc == realDesc) { "Description should be '$realDesc', but was '$desc'" }
    }

    @Test
    fun `Deactivate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val  scoreComponent = ScoreComponent(100)
        owner add scoreComponent
        val desc = (trapCard get EffectComponent::class).onDeactivate(owner)
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
            (trapCard get EffectComponent::class).onDeactivate(owner)
            (trapCard get ActivationCounterComponent::class).deactivate()
            (trapCard get EffectComponent::class).onPlay(owner)
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
    }

    @Test
    fun addShuffleTestCards() {
    }

    @Test
    fun addShovelCards() {
    }

    @Test
    fun addHealingCards() {
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