package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.managers.generateEntity

class CardBuilderSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {
    companion object {
        val instance: CardBuilderSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardBuilderSystem(ComponentManager.componentManager)
        }
    }
    var cardHealth: Float? = null
    var scoreAmount: Int? = null
    var cardAmount: Int = 1
    var cardImage: Int = R.drawable.placeholder
    var description: String = ""
    var name: String = ""
    var tags: List<CardTag> = listOf(CardTag.CARD)
    var onCardPlay: (Int, Int) -> Unit = { _, _ -> }
    var onCardDeactivate: (Int, Int) -> Unit = { _, _ -> }

    private fun initCards(): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        repeat(cardAmount) {
            val cardEntity = generateEntity()
            cardHealth?.let { health -> cardEntity add HealthComponent(health) }
            scoreAmount?.let { score -> cardEntity add ScoreComponent(score) }
            cardEntity add ActivationCounterComponent()
            cardEntity add ImageComponent(cardImage)
            cardEntity add EffectComponent(onDeactivate = onCardDeactivate, onPlay = onCardPlay)
            cardEntity add DescriptionComponent(description)
            cardEntity add NameComponent(name)
            cardEntity add TagsComponent(tags)
            cardIds.add(cardEntity)
        }
        return cardIds
    }

    fun buildCards(init: CardBuilderSystem.() -> Unit): List<Int> {
        val builder = CardBuilderSystem(componentManager)
        builder.init()
        return builder.initCards()

    }
}