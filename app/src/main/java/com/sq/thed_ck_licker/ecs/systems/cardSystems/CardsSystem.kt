package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
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
import com.sq.thed_ck_licker.ecs.systems.helperSystems.discardSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlin.math.min

class CardsSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {


    companion object {
        val instance: CardsSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardsSystem(ComponentManager.componentManager)
        }
    }


    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeck = entityId get DrawDeckComponent::class
        check(drawDeck.drawCardDeck.isNotEmpty()) { "No cards available" }
        val theCard = drawDeck.drawCardDeck.getRandomElement()
        drawDeck.drawCardDeck.remove(theCard)
        return theCard
    }

    fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {
        playerCardCount.intValue += 1
        val latestCardId = latestCard.intValue

        try {
            (latestCardId get EffectComponent::class).onPlay.invoke(getPlayerID(), latestCardId)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No effect component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no effect component"
            )
        }

        try {
            (latestCardId get HealthComponent::class).health -= 1f
            Log.i(
                "CardsSystem",
                "Health is now ${(latestCardId get HealthComponent::class).health}"
            )
            if ((latestCardId get HealthComponent::class).health <= 0) {
                latestCard.intValue = -1
            }
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No health component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no health component"
            )
        }

        try {
            (latestCardId get ActivationCounterComponent::class).activate()
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No actCounter component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no actCounter component"
            )
        }
        discardSystem(ownerId = getPlayerID(), cardId = latestCardId)
        latestCard.intValue = -1
    }

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3) {

        val gainerEntity = generateEntity()
        val gainerActCounterComp = ActivationCounterComponent()
        val activateAction = { id: Int ->
            val targetScoreComp = id get ScoreComponent::class
            gainerActCounterComp.activate()
            targetScoreComp.score += pointsPerCard
        }
        gainerEntity add gainerActCounterComp
        gainerEntity add EffectComponent(onTurnStart = activateAction)

        val targetEffectStackComp = (targetId get EffectStackComponent::class)
        targetEffectStackComp addEntity (gainerEntity)  // I think i have gone mad from the power
    }

    fun addLimitedSupplyAutoHealToEntity(targetEntityId: Int, health: Float) {
        val limitedHealEntity = generateEntity()
        val selfHp = HealthComponent(health)
        limitedHealEntity add selfHp
        val selfActCounter = ActivationCounterComponent()
        limitedHealEntity add selfActCounter

        limitedHealEntity add EffectComponent(onTurnStart = { id: Int ->
            val targetHealthComponent = id get HealthComponent::class
            val targetMaxHp = targetHealthComponent.maxHealth
            val targetHp = targetHealthComponent.health
            if (targetHp < targetMaxHp / 2) {
                val amountToHeal = (targetMaxHp * 0.8f) - targetHp
                val amountOfHealingProvided = min(selfHp.health, amountToHeal)
                selfHp.health -= amountOfHealingProvided
                targetHealthComponent.health += amountOfHealingProvided
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.health} health \nand have been activated ${selfActCounter.activations.intValue} times")
        })

        val targetEffectStackComp = (targetEntityId get EffectStackComponent::class)
        targetEffectStackComp addEntity (limitedHealEntity)
    }

    /**
     *  @param cardHealth Health of card
     *  @param scoreAmount Score of card
     *  @param amount Amount of cards to create
     *  @param cardImage Image of card
     *  @param description Description of card
     *  @param name Name of card
     *  @param tags Tags of card
     *  @param onCardPlay Action to perform when card is played
     *  @param onCardDeactivate Action to perform when card is deactivated
     */
    fun initCards(
        cardHealth: Float?,
        scoreAmount: Int?,
        amount: Int,
        cardImage: Int,
        description: String,
        name: String,
        tags: List<CardTag>,
        onCardPlay: (Int, Int) -> Unit = { _, _ -> },
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