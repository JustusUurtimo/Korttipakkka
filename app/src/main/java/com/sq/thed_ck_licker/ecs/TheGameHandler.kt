package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem
import com.sq.thed_ck_licker.ecs.systems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.PlayerSystem
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId

/*TODO apparently this kind a not good...
*  If you want I can do new refactor to make it better.
*  But I think this is good enough for now.
*  More about this https://developer.android.com/topic/architecture/ui-layer/stateholders
*  aa
*  Maybe this should be relicated for testing and prototyping
*  since the systems should handle all the things
 */
@Deprecated("Everything should go through their own systems any way")
object TheGameHandler {
    private val componentManager = ComponentManager.componentManager
    val cardsSystem = CardsSystem(componentManager)
    private val playerSystem = PlayerSystem(componentManager)
    private val descriptionSystem = DescriptionSystem(componentManager)
    val cardDisplaySystem = CardDisplaySystem(componentManager)
    val cardEffectSystem = CardEffectSystem(componentManager)



    fun getPlayerHealthM(): MutableFloatState {
        val player = playerId()
        val comp = componentManager.getComponent(player, HealthComponent::class)
        return comp.health
    }


    fun getTestingCardSequence(): Sequence<Int> {
        val eka = componentManager.getEntitiesWithTags(listOf(CardTag.CARD)).keys
        return eka.asSequence()
    }

    fun getPlayerScoreM(): MutableIntState {
        val player = playerId()
        val comp = componentManager.getComponent(player, ScoreComponent::class)
        return comp.score
    }

}