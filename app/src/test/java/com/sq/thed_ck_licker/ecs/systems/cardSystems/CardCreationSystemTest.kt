package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
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
    }

    @Test
    fun addTimeBoundTestCards() {
    }

    @Test
    fun addDamageCards() {
    }

    @Test
    fun addTrapTestCards() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        owner add HealthComponent()
        val desc = (trapCard get EffectComponent::class).onPlay(owner)
        println("desc: $desc")

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