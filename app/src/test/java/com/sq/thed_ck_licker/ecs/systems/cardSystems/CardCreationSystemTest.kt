package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems_Factory
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import dagger.internal.DaggerGenerated
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import kotlin.properties.Delegates

class CardCreationSystemTest {
    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()
    @BeforeEach
    fun setUp() {
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
        val trapCard = cardCreationSystem.addTrapTestCards(1)
        println("Trap card: $trapCard")

        val owner = EntityManager.createNewEntity()

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