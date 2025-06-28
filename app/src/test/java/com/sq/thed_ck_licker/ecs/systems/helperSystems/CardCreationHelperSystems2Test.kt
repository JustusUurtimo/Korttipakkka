package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
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
    var owner by Delegates.notNull<EntityId>()

    @BeforeEach
    fun setUp() {
        owner = EntityManager.createNewEntity()
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

}