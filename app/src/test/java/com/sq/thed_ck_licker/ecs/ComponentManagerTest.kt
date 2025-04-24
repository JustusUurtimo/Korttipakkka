package com.sq.thed_ck_licker.ecs

import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import org.junit.jupiter.api.Test

class ComponentManagerTest {

    @Test
    fun `Add component to entity`() {
        val entityId = 1
        val component = ScoreComponent(100)
        entityId add component
        val componentFromEntity = entityId get ScoreComponent::class
        assert(componentFromEntity.score == 100){"Score should be 100 but was ${componentFromEntity.score}"}
    }

    @Test
    fun `Copy Entity with Score Component`() {
        val entityId = EntityManager.createNewEntity()
        val component = ScoreComponent(100)
        entityId add component

        val componentManager = ComponentManager.componentManager
        val copiedEntity = componentManager.copy(entityId)

        val originalComponent = entityId get ScoreComponent::class
        originalComponent.score = 201

        val copiedComponent = copiedEntity get ScoreComponent::class

        assert(copiedComponent.score == 100) {"Score for the copied entity should be 100 but was ${copiedComponent.score}"}
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


    @Test
    fun `Get the difference between Health Components`() {
        val entityId = EntityManager.createNewEntity()
        val healthA = 100f
        entityId add HealthComponent(healthA)

        val entity2Id = EntityManager.createNewEntity()
        val healthB = 25f
        entity2Id add HealthComponent(healthB)

        val resultEntity = entityId difference entity2Id
        val healthDifference = resultEntity get HealthComponent::class
        assert(healthDifference.health == healthA - healthB) { "Health should be ${healthA - healthB} but was ${healthDifference.health}" }
        assert(healthDifference.maxHealth == healthA - healthB) { "Max Health should be ${healthA - healthB} but was ${healthDifference.maxHealth}" }

        val resultEntity2 = entity2Id difference entityId
        val healthDifference2 = resultEntity2 get HealthComponent::class
        assert(healthDifference2.health == healthB - healthA) { "Health should be ${healthB - healthA} but was ${healthDifference2.health}" }
        assert(healthDifference2.maxHealth == healthB - healthA) { "Max Health should be ${healthB - healthA} but was ${healthDifference.maxHealth}" }
    }

    @Test
    fun `Get the difference between Score Components`() {
        val entityId = EntityManager.createNewEntity()
        entityId add ScoreComponent(100)

        val entity2Id = EntityManager.createNewEntity()
        entity2Id add ScoreComponent(25)

        val resultEntity = entityId difference entity2Id
        val resultScore = resultEntity get ScoreComponent::class
        assert(resultScore.score == 75) { "Score should be 75 but was ${resultScore.score}" }

        val resultEntity2 = entity2Id difference entityId
        val scoreDifference = resultEntity2 get ScoreComponent::class
        assert(scoreDifference.score == -75) { "Score should be -75 but was ${scoreDifference.score}" }
    }


    @Test
    fun `Get the difference between Multiplier Components`() {
        val entityId = EntityManager.createNewEntity()
        val multiplierA = 1.5f
        entityId add MultiplierComponent(multiplierA)

        val entity2Id = EntityManager.createNewEntity()
        val multiplierB = 1f
        entity2Id add MultiplierComponent(multiplierB)

        val resultEntity = entityId difference entity2Id
        val resultMultiplierComp = resultEntity get MultiplierComponent::class
        assert(resultMultiplierComp.multiplier == multiplierA - multiplierB) { "Multiplier should be ${multiplierA - multiplierB} but was ${resultMultiplierComp.multiplier}" }

        val resultEntity2 = entity2Id difference entityId
        val resultMultiplierComp2 = resultEntity2 get MultiplierComponent::class
        assert(resultMultiplierComp2.multiplier == multiplierB - multiplierA) { "Multiplier should be ${multiplierB - multiplierA} but was ${resultMultiplierComp2.multiplier}" }
    }


    @Test
    fun `Get the difference between miss matching Components`() {
        val entityId = EntityManager.createNewEntity()
        val scoreA = 100
        entityId add ScoreComponent(scoreA)
        entityId add MultiplierComponent(1.5f)

        val entity2Id = EntityManager.createNewEntity()
        val scoreB = 25
        entity2Id add ScoreComponent(scoreB)
        entity2Id add HealthComponent(75f)

        val resultEntity = entityId difference entity2Id

        val resultScoreComp = resultEntity get ScoreComponent::class
        assert(resultScoreComp.score == scoreA - scoreB) { "Score should be ${scoreA - scoreB} but was ${resultScoreComp.score}" }

        var didCatchException = false
        try {
            resultEntity get HealthComponent::class
        } catch (_: IllegalStateException) {
            didCatchException = true
        }
        assert(didCatchException) { "Should have thrown an exception for HealthComponent" }

        didCatchException = false
        try {
            resultEntity get MultiplierComponent::class
        } catch (_: IllegalStateException) {
            didCatchException = true
        }
        assert(didCatchException) { "Should have thrown an exception for MultiplierComponent" }
    }

    @Test
    fun `Entity should not have score component with 0 values as difference2`() {
        testComponentDifferenceGeneric<ScoreComponent>(
            ScoreComponent(120),
            "Difference entity should not have a ScoreComponent"
        )
    }

    @Test
    fun `Entity should not have health component with 0 values as difference2`() {
        testComponentDifferenceGeneric<HealthComponent>(
            HealthComponent(120f),
            "Difference entity should not have a HealthComponent"
        )
    }

    @Test
    fun `Entity should not have multiplier component with 0 values as difference2`() {
        testComponentDifferenceGeneric<MultiplierComponent>(
            MultiplierComponent(12f),
            "Difference entity should not have a MultiplierComponent"
        )
    }

    inline fun <reified T : Any> testComponentDifferenceGeneric(
        component: T,
        assertionMessage: String
    ) {
        val entityId = EntityManager.createNewEntity()
        entityId add component

        val entity2Id = EntityManager.createNewEntity()
        entity2Id add component

        val resultEntity = entityId difference entity2Id
        var didCatchException = false
        try {
            resultEntity.get(T::class)
        } catch (_: IllegalStateException) {
            didCatchException = true
        }
        assert(didCatchException) { assertionMessage }
    }



    @Test
    fun `Replace component of entity`() {
        val entityId = EntityManager.createNewEntity()
        entityId add ScoreComponent(100)
        entityId add ScoreComponent(200)
        val componentFromEntity = entityId get ScoreComponent::class
        assert(componentFromEntity.score == 200){"Score should be 200 but was ${componentFromEntity.score}"}
    }


}


