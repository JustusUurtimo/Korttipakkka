package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.activate
import com.sq.thed_ck_licker.ecs.components.addEntity
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlin.math.min

class CardsSystem private constructor(private val componentManager: ComponentManager) {


    companion object {
        val instance: CardsSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardsSystem(ComponentManager.componentManager)
        }
    }

    /* TODO: multipliers and such can be nicely implemented with:
*   Make each effect function that takes in the thing  to be multiplied.
*   Then just pump all things through pipeline full of the multiplier functions.
*   To get the pipeline just loop all things and collect the multiplier functions.
*   Something like multiplier system.
*   If one wants to be wild they can even put three lists as pipeline, one for additions, one for increases and one for multiplications
*   Thou that would mean order of operation does not matter, which might be wanted.
*/


    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeck = entityId get DrawDeckComponent::class
        check(drawDeck.drawCardDeck.isNotEmpty()) { "No cards available" }
        return drawDeck.drawCardDeck.getRandomElement()
    }

    fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {

        playerCardCount.intValue += 1
        val latestCardId = latestCard.intValue

        try {
            (latestCardId get EffectComponent::class).onPlay.invoke(getPlayerID(), latestCardId)
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no effect component")
        }

        try {
            (latestCardId get HealthComponent::class).health.floatValue -= 1f
            println("Health is now ${(latestCardId get HealthComponent::class).health.floatValue}")
            if ((latestCardId get HealthComponent::class).health.floatValue <= 0) {
                latestCard.intValue = -1
            }
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no health component")
        }

        try {
            (latestCardId get ActivationCounterComponent::class).activate()
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no actCounter component ")
        }
    }

    fun addPassiveScoreGainerToThePlayer(playerId: Int, pointsPerCard: Int = 3) {

        val entity = generateEntity()
        val activationComponent = ActivationCounterComponent()
        val activateAction = { id: Int ->
            val playerScoreComponent = id get ScoreComponent::class
            activationComponent.activate()
            playerScoreComponent.score.intValue += pointsPerCard
        }
        entity add activationComponent
        entity add EffectComponent(onTurnStart = activateAction)
        val effStackComp = (playerId get EffectStackComponent::class)
        effStackComp addEntity (entity)  // I think i have gone mad from the power
    }

    fun addLimitedSupplyAutoHealToThePlayer(playerId: Int, health: Float) {
        val entity = generateEntity()
        val selfHp = HealthComponent(health)
        entity add selfHp
        val selfActCounter = ActivationCounterComponent()
        entity add selfActCounter

        entity add EffectComponent(onTurnStart = { id: Int ->
            val targetHealthComponent = id get HealthComponent::class
            val targetMaxHp = targetHealthComponent.maxHealth.floatValue
            val targetHp = targetHealthComponent.health.floatValue
            if (targetHp < targetMaxHp / 2) {
                val healAmount = (targetMaxHp * 0.8f) - targetHp
                val amount = min(selfHp.health.floatValue, healAmount)
                selfHp.health.floatValue -= amount
                targetHealthComponent.health.floatValue += amount
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.health.floatValue} health \nand have been activated ${selfActCounter.activations.intValue} times")
        })

        val effStackComp = (playerId get EffectStackComponent::class)
        effStackComp addEntity (entity)
    }

    fun initCards(
        cardHealth: Float?,
        scoreAmount: Int?,
        amount: Int,
        cardImage: Int,
        description: String,
        name: String,
        tags: List<CardTag>,
        onCardPlay: (Int, Int) -> Unit = {_, _ ->},
        onCardDeactivate: (Int, Int) -> Unit = { _, _ -> }
    ): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        repeat(amount) {
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

}