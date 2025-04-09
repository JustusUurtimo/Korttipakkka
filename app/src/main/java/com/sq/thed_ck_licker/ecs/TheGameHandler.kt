package com.sq.thed_ck_licker.ecs

import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem
import com.sq.thed_ck_licker.ecs.systems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.PlayerSystem

/**
 * Everything should go through their own systems any way
 * Thou there may be argument to put things here until they find their actual home
 */
object TheGameHandler {
    private val componentManager = ComponentManager.componentManager
    val cardsSystem = CardsSystem(componentManager)
    internal val playerSystem = PlayerSystem(componentManager)
    private val descriptionSystem = DescriptionSystem(componentManager)
    val cardDisplaySystem = CardDisplaySystem(componentManager)
    val cardEffectSystem = CardEffectSystem(componentManager)


//    fun getTestingCardSequence(): Sequence<Int> {
//        val eka = componentManager.getEntitiesWithTags(listOf(CardTag.CARD)).keys
//        return eka.asSequence()
//    }

}