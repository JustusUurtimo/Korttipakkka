package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem_Factory
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class CardCreationHelperSystems2Test {
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
    fun addPassiveScoreGainerToEntity() {
        val scoreComp = ScoreComponent(0)
        owner add scoreComp

        val gainer = CardCreationHelperSystems2.addPassiveScoreGainerToEntity(owner)

        val context = EffectContext(
            trigger = Trigger.OnTurnStart,
            source = gainer,
            target = owner,
        )

        repeat(10) {
            TriggerEffectHandler.handleTriggerEffect(context)
        }

        assert(scoreComp.getScore() == 30) { "Score should be 30, but was ${scoreComp.getScore()}" }
    }


    @Test
    fun `Limited time heal activates`() {
        val healthComponent = HealthComponent(100f, 1000f)
        owner add healthComponent

        val gainer = CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(owner, 100f)

        val context = EffectContext(
            trigger = Trigger.OnTurnStart,
            source = gainer,
            target = owner,
        )

        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 200f) { "Health should be 200, but was ${healthComponent.getHealth()}" }
    }

    @Test
    fun `take damage to activate limited heal`() {
        val healthComponent = HealthComponent(500f, 1000f)
        owner add healthComponent

        val gainer = CardCreationHelperSystems2.addLimitedSupplyAutoHealToEntity(owner, 100f)

        val damage = cardCreationSystem.addDamageCards(1).first()

        val context = EffectContext(
            trigger = Trigger.OnTurnStart,
            source = gainer,
            target = owner,
        )

        val context2 = EffectContext(
            trigger = Trigger.OnPlay,
            source = damage,
            target = owner,
        )


        TriggerEffectHandler.handleTriggerEffect(context)
        assert(healthComponent.getHealth() == 500f) { "Health should be 500, but was ${healthComponent.getHealth()}" }

        TriggerEffectHandler.handleTriggerEffect(context2)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 450f) { "Health should be 450, but was ${healthComponent.getHealth()}" }
    }


    @Test
    fun addTemporaryMultiplierTo() {
        val scoreComp = ScoreComponent(0)
        owner add scoreComp

        val hpComp = HealthComponent(500f, 1000f)
        owner add hpComp

        owner add MultiplierComponent(1f)

        val multiEntity = CardCreationHelperSystems2.addTemporaryMultiplierTo(
            owner,
            health = 10f,
            multiplier = 2.8f
        )
        val multiContext = EffectContext(
            trigger = Trigger.OnTurnStart,
            source = multiEntity,
            target = owner,
        )

        val scoreCard = cardCreationSystem.addBasicScoreCards(1, 100).first()
        val scoreContext = EffectContext(
            trigger = Trigger.OnPlay,
            source = scoreCard,
            target = owner,
        )
        val healCard = cardCreationSystem.addHealingCards(1, 100f).first()
        val healContext = EffectContext(
            trigger = Trigger.OnPlay,
            source = healCard,
            target = owner,
        )
        val damageCard = cardCreationSystem.addDamageCards(1, 100f).first()
        val damageContext = EffectContext(
            trigger = Trigger.OnPlay,
            source = damageCard,
            target = owner,
        )

        repeat(20) {
            try {
                TriggerEffectHandler.handleTriggerEffect(multiContext)
            } catch (_: Exception) {
            }
            TriggerEffectHandler.handleTriggerEffect(scoreContext)
            TriggerEffectHandler.handleTriggerEffect(healContext)
            if (it == 4) {
                assert(hpComp.getHealth() == 780f) { "Health should be 780, but was ${hpComp.getHealth()}" }
            }
            if (it == 14) {
                assert(hpComp.getHealth() == 600f) { "Health should be 600, but was ${hpComp.getHealth()}" }
            }
            TriggerEffectHandler.handleTriggerEffect(damageContext)

            DeathSystem.checkForDeath()
        }

        assert(scoreComp.getScore() == 3800) { "Score should be 3800, but was ${scoreComp.getScore()}" }

    }

}