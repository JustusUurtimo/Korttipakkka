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


    @Test
    fun `Copy Entity with Score Component`() {
        val entityId = EntityManager.createNewEntity()
        val component = ScoreComponent(100)
        entityId add component

        val componentManager = ComponentManager.componentManager
        val copiedEntity = componentManager.copy(entityId)

        val originalComponent = entityId get ScoreComponent::class
        originalComponent.setScore(201)

        val copiedComponent = copiedEntity get ScoreComponent::class

        assert(copiedComponent.getScore() == 100) {"Score for the copied entity should be 100 but was ${copiedComponent.getScore()}"}
        assert(originalComponent != copiedComponent) {"Original and copied components should not be the same after the change."}
    }

}