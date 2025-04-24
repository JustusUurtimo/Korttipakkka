package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.get
import org.junit.jupiter.api.Test

class MultiplicationSystemKtTest {
    @Test
    fun `Apply Multiplier Component To Score Component`() {
        val entityId = EntityManager.createNewEntity()
        val multiplier = 1.75f
        entityId add MultiplierComponent(multiplier)
        val scoreA = 100
        entityId add ScoreComponent(scoreA)

        val componentManager = ComponentManager.componentManager
        val oldEntity = componentManager.copy(entityId)

        (entityId get ScoreComponent::class).score += scoreA

        var scoreComponent = (entityId get ScoreComponent::class)
        val scoreSum = scoreA + scoreA
        assert(scoreComponent.score == scoreSum) { "Score should be $scoreSum, but was ${scoreComponent.score}" }

        multiplyEntityValues(oldEntity, entityId)

        scoreComponent = (entityId get ScoreComponent::class)
        val correctScore = scoreA + (scoreA.toFloat() * multiplier).toInt()
//        assert((entityId get ScoreComponent::class).score == correctScore) { "Score should be $correctScore, but was ${scoreComponent.score}" }
    }
}