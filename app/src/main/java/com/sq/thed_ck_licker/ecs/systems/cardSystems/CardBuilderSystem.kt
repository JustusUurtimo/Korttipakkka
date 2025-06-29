package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.helpers.DescribedEffect
import javax.inject.Inject

@Deprecated("As of 0.1.2.148, Use CardBuilderSystem2 instead")
class CardBuilderSystem @Inject constructor(private val componentManager: ComponentManager) {

    var cardHealth: Float? = null
    var scoreAmount: Int? = null
    var characterId: Int? = null
    var cardAmount: Int = 1
    var cardImage: Int = R.drawable.placeholder
    var name: String = ""
    var tags: List<CardTag> = listOf(CardTag.CARD)
    var onCardPlay: DescribedEffect = DescribedEffect({}, { "" })
    var onCardDeactivate: DescribedEffect = DescribedEffect({}, { "" })

    private fun initCards(): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        repeat(cardAmount) {
            val cardEntity = generateEntity()
            cardHealth?.let { health -> cardEntity add HealthComponent(health) }
            scoreAmount?.let { score -> cardEntity add ScoreComponent(score) }
            cardEntity add ActivationCounterComponent()
            cardEntity add ImageComponent(cardImage)
            cardEntity add EffectComponent(onDeactivate = onCardDeactivate, onPlay = onCardPlay)
            cardEntity add IdentificationComponent(name, characterId)
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

