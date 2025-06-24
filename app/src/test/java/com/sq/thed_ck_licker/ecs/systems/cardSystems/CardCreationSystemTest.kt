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

        val desc = (breakingCard get EffectComponent::class).onPlay.describe(owner)
        (breakingCard get EffectComponent::class).onPlay.action(owner)

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
    fun `Deactivation cards descriptions on creation`() {
        val deactivationCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(deactivationCard))
        val hpComponent = HealthComponent(1000f)
        owner add hpComponent
        val scoreComponent = ScoreComponent(1000)
        owner add scoreComponent
        var nextStep = 2
        var currentDamage = 1
        val multiplier = 30


        val realOnPlayDesc =
            "Gain ${multiplier}x points based on health lost from this card (${multiplier * currentDamage} points). Resets on use"
        val onPlayDesc = (deactivationCard get EffectComponent::class).onPlay.describe(owner)
        assert(onPlayDesc == realOnPlayDesc) { "1: Description on play should be\n'$realOnPlayDesc', but was\n'$onPlayDesc'" }

        val realOnDeactivateDesc =
            "Lose health (${currentDamage} points), increases damage by $nextStep"
        val onDeactivateDesc =
            (deactivationCard get EffectComponent::class).onDeactivate.describe(owner)
        assert(onDeactivateDesc == realOnDeactivateDesc) { "2: Description on deactivation should be\n'$realOnDeactivateDesc', but was\n'$onDeactivateDesc'" }
    }

    @Test
    fun `Deactivate Deactivation card once`() {
        val deactivationCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(deactivationCard))
        val hpComponent = HealthComponent(1000f)
        owner add hpComponent
        val scoreComponent = ScoreComponent(1000)
        owner add scoreComponent

        (deactivationCard get ActivationCounterComponent::class).deactivate()
        (deactivationCard get EffectComponent::class).onDeactivate.action(owner)
        assert(hpComponent.getHealth() == 999f) {
            "Health should be 999, but was ${hpComponent.getHealth()}"
        }
        assert(scoreComponent.getScore() == 1000) {
            "Score should be 1000, but was ${scoreComponent.getScore()}"
        }
    }


    @Test
    fun `Only activate Deactivation card once`() {
        val thousand = 1000
        val deactivationCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(deactivationCard))
        val hpComponent = HealthComponent(thousand.toFloat())
        owner add hpComponent
        val scoreComponent = ScoreComponent(thousand)
        owner add scoreComponent


        (deactivationCard get ActivationCounterComponent::class).activate()
        (deactivationCard get EffectComponent::class).onPlay.action(owner)
        val hp = hpComponent.getHealth()
        assert(hp == thousand.toFloat()) { "Health should be$thousand, but was $hp" }
        val score = scoreComponent.getScore()
        assert(score == thousand) { "Score should be $thousand, but was $score" }
    }

        @Test
    fun `Deactivate and activate Deactivation card once`() {
        val deactivationCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(deactivationCard))
        val hpComponent = HealthComponent(1000f)
        owner add hpComponent
        val scoreComponent = ScoreComponent(1000)
        owner add scoreComponent
        var nextStep = 2
        var currentDamage = 1
        val multiplier = 30


        val realOnPlayDesc =
            "Gain ${multiplier}x points based on health lost from this card (${multiplier * currentDamage} points). Resets on use"
        val onPlayDesc = (deactivationCard get EffectComponent::class).onPlay.describe(owner)
        assert(onPlayDesc == realOnPlayDesc) { "1: Description on play should be\n'$realOnPlayDesc', but was\n'$onPlayDesc'" }

        val realOnDeactivateDesc =
            "Lose health (${currentDamage} points), increases damage by $nextStep"
        val onDeactivateDesc =
            (deactivationCard get EffectComponent::class).onDeactivate.describe(owner)
        assert(onDeactivateDesc == realOnDeactivateDesc) { "2: Description on deactivation should be\n'$realOnDeactivateDesc', but was\n'$onDeactivateDesc'" }


        (deactivationCard get ActivationCounterComponent::class).deactivate()
        (deactivationCard get EffectComponent::class).onDeactivate.action(owner)
        var hp = hpComponent.getHealth()
        assert(hp == 999f) { "1: Health should be 999, but was $hp" }
        var score = scoreComponent.getScore()
        assert(score == 1000) { "1: Score should be 1000, but was $score" }


        (deactivationCard get ActivationCounterComponent::class).activate()
        (deactivationCard get EffectComponent::class).onPlay.action(owner)
        hp = hpComponent.getHealth()
        assert(hp == 999f) { "2: Health should be 999, but was $hp" }
        score = scoreComponent.getScore()
        assert(score == 1030) { "2: Score should be 1030, but was $score" }


        val realOnPlayDesc2 =
            "Gain ${multiplier}x points based on health lost from this card (${multiplier * currentDamage} points). Resets on use"
        val onPlayDesc2 = (deactivationCard get EffectComponent::class).onPlay.describe(owner)
        assert(onPlayDesc2 == realOnPlayDesc2) { "3: Description on play should be\n'$realOnPlayDesc2', but was\n'$onPlayDesc2'" }

        val realOnDeactivateDesc2 =
            "Lose health (${currentDamage} points), increases damage by $nextStep"
        val onDeactivateDesc2 =
            (deactivationCard get EffectComponent::class).onDeactivate.describe(owner)
        assert(onDeactivateDesc2 == realOnDeactivateDesc2) { "4: Description on deactivation should be\n'$realOnDeactivateDesc2',\n but \n'$onDeactivateDesc2'" }
    }

    @Test
    fun addTempMultiplierTestCards() {
    }

    @Test
    fun addMerchantCards() {
    }

}