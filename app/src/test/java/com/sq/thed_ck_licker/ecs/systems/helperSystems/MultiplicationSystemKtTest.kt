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
        entityId add MultiplierComponent(2f)
        entityId add ScoreComponent(100)

        val componentManager = ComponentManager.componentManager
        val oldEntity = componentManager.copy(entityId)

        (entityId get ScoreComponent::class).score += 100

        var scoreComponent = (entityId get ScoreComponent::class)
        assert((entityId get ScoreComponent::class).score == 200) {"Score should be 200, but was ${scoreComponent.score}"}

        multiplyEntityValues(oldEntity, entityId)

        scoreComponent = (entityId get ScoreComponent::class)
//        assert(scoreComponent.score == 300) {"Score should be 300, but was ${scoreComponent.score}"}
    }
}