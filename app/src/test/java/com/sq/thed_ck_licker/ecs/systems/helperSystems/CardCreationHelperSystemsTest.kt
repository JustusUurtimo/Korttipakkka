package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

class CardCreationHelperSystemsTest {
    var cardCreationHelperSystems by Delegates.notNull<CardCreationHelperSystems>()
    var owner by Delegates.notNull<EntityId>()
    @BeforeEach
    fun setUp() {
        cardCreationHelperSystems = CardCreationHelperSystems_Factory.newInstance()
        owner = EntityManager.createNewEntity()
    }

    @Test
    fun addLimitedSupplyAutoHealToEntity() {
    }

    @Test
    fun addPassiveScoreGainerToEntity() {
    }

    @Test
    fun addTemporaryMultiplierTo() {
        cardCreationHelperSystems.addTemporaryMultiplierTo(
            targetEntityId = owner,
            health = 10f,
            multiplier = 6f
        )
    }

}