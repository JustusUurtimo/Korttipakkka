package com.sq.thed_ck_licker.ecs.managers

import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import org.junit.jupiter.api.Test

class ComponentManagerTest {

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


    @Test
    fun `Copy Entity with Effect Component`() {
        val entityId = EntityManager.createNewEntity()
        var counter = 0
        val onPlay: (Int, Int) -> Unit = { _, _ -> counter++ }
        val component = EffectComponent(onPlay = onPlay)
        entityId add component

        val componentManager = ComponentManager.componentManager
        val copiedEntity = componentManager.copy(entityId)

        val originalComponent = entityId get EffectComponent::class
        originalComponent.onPlay.invoke(0, 0)

        val copiedComponent = copiedEntity get EffectComponent::class
        repeat(4) {
            copiedComponent.onPlay.invoke(0, 0)
        }

        assert(counter == 5) { "Counter should be 5 but was $counter" }
        assert(originalComponent == copiedComponent) { "Original and copied components should be the same after the change." }
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
        assert(healthDifference.getHealth() == healthA - healthB) { "Health should be ${healthA - healthB} but was ${healthDifference.getHealth()}" }
        assert(healthDifference.getMaxHealth() == healthA - healthB) { "Max Health should be ${healthA - healthB} but was ${healthDifference.getMaxHealth()}" }

        val resultEntity2 = entity2Id difference entityId
        val healthDifference2 = resultEntity2 get HealthComponent::class
        assert(healthDifference2.getHealth() == healthB - healthA) { "Health should be ${healthB - healthA} but was ${healthDifference2.getHealth()}" }
        assert(healthDifference2.getMaxHealth() == healthB - healthA) { "Max Health should be ${healthB - healthA} but was ${healthDifference.getMaxHealth()}" }
    }

    @Test
    fun `Get the difference between Score Components`() {
        val entityId = EntityManager.createNewEntity()
        entityId add ScoreComponent(100)

        val entity2Id = EntityManager.createNewEntity()
        entity2Id add ScoreComponent(25)

        val resultEntity = entityId difference entity2Id
        val resultScore = resultEntity get ScoreComponent::class
        assert(resultScore.getScore() == 75) { "Score should be 75 but was ${resultScore.getScore()}" }

        val resultEntity2 = entity2Id difference entityId
        val scoreDifference = resultEntity2 get ScoreComponent::class
        assert(scoreDifference.getScore() == -75) { "Score should be -75 but was ${scoreDifference.getScore()}" }
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
        assert(resultScoreComp.getScore() == scoreA - scoreB) { "Score should be ${scoreA - scoreB} but was ${resultScoreComp.getScore()}" }

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
        assert(componentFromEntity.getScore() == 200){"Score should be 200 but was ${componentFromEntity.getScore()}"}
    }

    @Test
    fun `Add two score components together`() { //Ed...ward...Eed...ward....
        val scoreComponent = ScoreComponent(100)
        val scoreComponent2 = ScoreComponent(200)
        val combinedComponent = scoreComponent.combineScoreComponents(scoreComponent2)
        assert(combinedComponent.getScore() == 300){"Score should be 300 but was ${combinedComponent.getScore()}"}
    }


    @Test
    fun `Get the difference between two Effect components`() {
        var counter = 0
        val onCardPlay: (Int, Int) -> Unit = { _, _ -> counter++ }
        val effComp = EffectComponent(
            onPlay = onCardPlay,
        )

        effComp.onPlay.invoke(0, 0)
        assert(counter == 1) { "Counter should be 1 but was $counter" }

        val onCardPlay2: (Int, Int) -> Unit = { _, _ -> counter += 100 }
        val effComp2 = EffectComponent(
            onPlay = onCardPlay2
        )
        effComp2.onPlay.invoke(0, 0)
        assert(counter == 101) { "Counter should be 101 but was $counter" }

        val eka = EntityManager.createNewEntity()
        eka add effComp

        val eka2 = EntityManager.createNewEntity()
        eka2 add effComp2

        val diff = eka difference eka2
        val diffEff = diff get EffectComponent::class
        diffEff.onPlay.invoke(0, 0)
        assert(counter == 202) { "Counter should be 202 but was $counter" }
    }

}