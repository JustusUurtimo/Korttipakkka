package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.systems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.PlayerSystem
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId

//TODO apparently this kind a not good...
// If you want I can do new refactor to make it better.
// But I think this is good enough for now.
// More about this https://developer.android.com/topic/architecture/ui-layer/stateholders
object TheGameHandler {
    val cardsSystem = CardsSystem()
    private val componentManager = ComponentManager()
    private val playerSystem = PlayerSystem()
    private val descriptionSystem = DescriptionSystem()


    fun getPlayerHealthM(): MutableFloatState {
        val player = playerId()
        val comp = componentManager.getComponent(player, HealthComponent::class)
        return comp.health
    }


//    fun getEntityHealth(entityId: Int): Float {
//        val comp = componentManager.getComponent(entityId, HealthComponent::class)
//        return comp.health.floatValue
//    }
//
//    fun getEntityHealthM(entityId: Int): MutableFloatState {
//        val comp = componentManager.getComponent(entityId, HealthComponent::class)
//        return comp.health
//    }


    // TODO this is just temporary
    //  it is meant only for brief debugging thing
    //  it is really baad
    @Deprecated("It is so baad that it should be already deprecated")
    fun getRandomCard(): Map<Int, Any> {
        return componentManager.getEntitiesWithTags(listOf(CardTag.CARD))
    }


    fun initTheGame() {
        playerSystem.initPlayer(componentManager)
        descriptionSystem.updateAllDescriptions(componentManager)
    }

    fun getComponents(entityId: Int): List<Any> {
        val aaa = componentManager.getAllComponentsOfEntity(entityId)
        return aaa
    }

    // TODO this is just temporary
    //  it is meant only for brief debugging thing
    //  it is really baad
    fun getTheComponents(): ComponentManager {
        return componentManager
    }

    fun getPlayerScoreM(): MutableIntState {
        val player = playerId()
        val comp = componentManager.getComponent(player, ScoreComponent::class)
        return comp.score
    }

}