package com.sq.thed_ck_licker.ecs.managers

import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import org.junit.jupiter.api.AfterEach
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
    fun `Add multiply component to entity`() {
        val entityId = EntityManager.createNewEntity()
        val multi = 2f
        val component = MultiplierComponent(multi)
        entityId add component
        val componentFromEntity = entityId get MultiplierComponent::class
        assert(componentFromEntity.multiplier == multi){"Multiplier should be $multi but was ${componentFromEntity.multiplier}"}
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

    @Test
    fun `Copy Entity with Multiplier Component`() {
        val entityId = EntityManager.createNewEntity()
        val component = MultiplierComponent(1.1f)
        entityId add component

        val componentManager = ComponentManager.componentManager
        val copiedEntity = componentManager.copy(entityId)

        val originalComponent = entityId get MultiplierComponent::class
        originalComponent.timesMultiplier(1.5f)

        val copiedComponent = copiedEntity get MultiplierComponent::class

        assert(copiedComponent.multiplier == 1.1f) {"Multiplier for the copied entity should be 1.1f but was ${copiedComponent.multiplier}"}
        assert(originalComponent != copiedComponent) {"Original and copied components should not be the same after the change."}
    }

}