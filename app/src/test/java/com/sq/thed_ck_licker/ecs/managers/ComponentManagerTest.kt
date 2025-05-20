package com.sq.thed_ck_licker.ecs.managers

import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ComponentManagerTest {
    @BeforeEach
    fun setUp() {
//        TODO("Not yet implemented")
    }

    @AfterEach
    fun tearDown() {
//        TODO("Not yet implemented")
    }

    @Test
    fun `Add component to entity`() {
        val entityId = 1
        val component = ScoreComponent(100)
        entityId add component
        val componentFromEntity = entityId get ScoreComponent::class
        assert(componentFromEntity.getScore() == 100){"Score should be 100 but was ${componentFromEntity.getScore()}"}
    }

}