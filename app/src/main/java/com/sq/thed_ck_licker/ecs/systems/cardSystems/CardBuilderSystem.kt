package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
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
import javax.inject.Inject

class CardBuilderSystem @Inject constructor(private val componentManager: ComponentManager) {

    var cardHealth: Float? = null
    var scoreAmount: Int? = null
    var characterId: Int? = null
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

    fun addCompToManyCards(comp: Any, list: List<Int>): List<Int> {
        list.forEach { it add comp }
        return list
    }

    fun addTimeBoundTestCards2(numberOfCards: Int = 1, amountOfDamage: Float = 1f): List<EntityId> {
        val cards = mutableListOf<EntityId>()
        println("kissa")
        repeat(numberOfCards) {
            println("wad?")
            val entityrrr: EntityId = EntityManager.createNewEntity()
            val selfCounter =
                ActivationCounterComponent() // This spells problems... i need to make tests for it
            val selfHp = HealthComponent(300f)

            val onTick = { target: Int ->
                println("Should I be ticking?")
                if (selfCounter.getActivations() > 0) {
                    val targetHealth = target get HealthComponent::class
                    println("Ticking")
                    println("Health is ${targetHealth.getHealth()}")
                    println("Target is $target")
                    targetHealth.damage(amountOfDamage)
                    if (targetHealth.getHealth() <= 0) {
                        onDeathSystem()
                    }
                }
            }

            val onActivation = { targetId: Int, _: Int ->
                val scoreComponent = targetId get ScoreComponent::class
                Log.i("Time Bound Activation", "Activation number: ${selfCounter.getActivations()}")
                if (selfCounter.getActivations() == 3) {
                    scoreComponent.addScore(10000)
                    Log.i("Time Bound Activation", "Score is ${scoreComponent.getScore()}")
                    selfHp.damage(300f)
                }

                if (selfCounter.getActivations() == 1) {
                    entityrrr add TickComponent(
                        currentAmount = 0,
                        tickThreshold = 100,
                        tickAction = onTick
                    )
                }
            }


            entityrrr add selfCounter
            entityrrr add ImageComponent(cardImage)
            entityrrr add EffectComponent(onPlay = onActivation)
            entityrrr add DescriptionComponent("If you manage to activate this card 3 times you will gain 10000 points")
            entityrrr add IdentificationComponent("Time Bound Card", null)
            entityrrr add selfHp
            cards.add(entityrrr)
        }
        return cards
    }
}