package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem
import com.sq.thed_ck_licker.helpers.DescribedEffect
import javax.inject.Inject

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


    fun createTimeBoundCards(numberOfCards: Int = 1): List<EntityId> {
        val cards = mutableListOf<EntityId>()
        val hp = 33f
        val points = 10000
        val times = 3
        repeat(numberOfCards) {
            val timeBoundCardId: EntityId = EntityManager.createNewEntity()
            val selfCounter =
                ActivationCounterComponent()
            val selfHp = HealthComponent(hp)

            val onTick = { target: Int ->
                if (selfCounter.getActivations() > 0) {
                    val targetHealth = target get HealthComponent::class
                    targetHealth.damage(1f)
                    if (targetHealth.getHealth() <= 0) {
                        onDeathSystem()
                    }
                }
            }

            val onTickEffect =
                DescribedEffect(onTick) { "Takes damage" }


            val onActivation = { targetId: Int ->
                val scoreComponent = targetId get ScoreComponent::class
                Log.i("Time Bound Activation", "Activation number: ${selfCounter.getActivations()}")
                if (selfCounter.getActivations() == times - 1) {
                    scoreComponent.addScore(points)
                    Log.i("Time Bound Activation", "Score is ${scoreComponent.getScore()}")
                    selfHp.damage(hp)
                }

                if (selfCounter.getActivations() == 0) {
                    timeBoundCardId add TickComponent(
                        currentAmount = 0,
                        tickThreshold = 1000,
                        tickAction = onTickEffect
                    )
                }
            }
            val onActivationEffect =
                DescribedEffect(onActivation) { "If you manage to activate this cards effect $times times, you will gain $points points" }


            timeBoundCardId add selfCounter
            timeBoundCardId add ImageComponent(cardImage)
            timeBoundCardId add EffectComponent(onPlay = onActivationEffect)
            timeBoundCardId add IdentificationComponent("Time Bound Card", null)
            timeBoundCardId add selfHp
            cards.add(timeBoundCardId)
        }
        return cards
    }
}