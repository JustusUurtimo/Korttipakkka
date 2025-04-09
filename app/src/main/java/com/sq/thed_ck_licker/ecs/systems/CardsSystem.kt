package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
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
import com.sq.thed_ck_licker.ecs.components.deactivate
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem.Companion.cardEffectSystem
import com.sq.thed_ck_licker.helpers.getRandomElement

class CardsSystem(private val componentManager: ComponentManager) {

    companion object {
        val cardsSystem: CardsSystem = CardsSystem(ComponentManager.componentManager)
    }

    fun <T : Any> initCards(
        amount: Int,
        cardImage: Int,
        description: String,
        name: String,
        tags: List<CardTag>,
        cardComponent: T
    ): List<Int> {
        val cardIds: MutableList<Int> = mutableListOf()
        repeat(amount) {
            val cardEntity = generateEntity()
            componentManager.addComponent(cardEntity, ImageComponent(cardImage))
            componentManager.addComponent(cardEntity, cardComponent)
            componentManager.addComponent(cardEntity, DescriptionComponent(description))
            componentManager.addComponent(cardEntity, NameComponent(name))
            componentManager.addComponent(cardEntity, TagsComponent(tags))
            cardIds.add(cardEntity)
        }
        return cardIds
    }

    // Function to pull a random card from deck
    fun pullRandomCardFromEntityDeck(entityId: Int, latestCard: MutableIntState) {
        val drawDeck = componentManager.getComponent(entityId, DrawDeckComponent::class)
        if (drawDeck.cardIds.isEmpty()) {
            throw IllegalStateException("No cards available")
        }
        latestCard.intValue = drawDeck.cardIds.getRandomElement()
    }



    fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {

        playerCardCount.intValue += 1

        try {
            (latestCard.intValue get EffectComponent::class).onPlay.invoke(getPlayerID())
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no effect component ")
            cardEffectSystem.playerTargetsPlayer(latestCard.intValue)
        }

        try {
            (latestCard.intValue get ActivationCounterComponent::class).activate()
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no actCounter component ")
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


    fun addDefaultCards(amount: Int = 7): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            val omaScore = ScoreComponent(10 * i)
            componentManager.addComponent(cardEntity, ImageComponent())
            componentManager.addComponent(cardEntity, omaScore)
            componentManager.addComponent(
                cardEntity, DescriptionComponent()
            )
            componentManager.addComponent(cardEntity, NameComponent("Default Card #$i"))
            componentManager.addComponent(cardEntity, TagsComponent(listOf(CardTag.CARD)))

            val selfAct = ActivationCounterComponent()
            cardEntity add selfAct
            val scoreIt = { id: Int ->
                val target = id get ScoreComponent::class
                target.score.intValue += omaScore.score.intValue
                //TODO: This certainly is not right way of doing this,
                // it should be handled in some general way, maybe some event based thing or system for them
            }

            cardEntity add EffectComponent(onPlay = scoreIt)
        }
        return cardIds
    }

    fun addDeactivationTestCards(amount: Int = 2): MutableList<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()

        val omaScore = ScoreComponent()
        val deactivateAction = { id: Int ->
            val target = id get HealthComponent::class
            omaScore.score.intValue += 1
            target.health.floatValue += omaScore.score.intValue.toFloat()
            println("Now its deactivated")
            println("Risk is rising!")
            println("Holds ${omaScore.score.intValue} points")

        }
        val onActivation = { id: Int ->
            val target = id get ScoreComponent::class
            val asd = omaScore.score.intValue * 3
            target.score.intValue += (asd)
            omaScore.score.intValue = 0
            println("Now its activated")
            println("Gave ${asd} points")
        }
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose health, on activation you gain score * 3")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Deactivation Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
            cardEntity add omaScore
        }
        return cardIds
    }

    fun addTrapTestCard(amount: Int = 2): MutableList<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()

        var scoreLoss = 0
        var healthLoss = 0
        val activationComponent = ActivationCounterComponent()
        val deactivateAction = { id: Int ->
            val target = id get ScoreComponent::class
            scoreLoss = ((1 + activationComponent.deactivations.intValue) * 3)
            target.score.intValue -= (activationComponent.deactivations.intValue * 3)
            println("Lost ${scoreLoss} points")
        }
        val onActivation = { id: Int ->
            val target = id get HealthComponent::class
            healthLoss = activationComponent.activations.intValue + 1
            target.health.floatValue += activationComponent.activations.intValue

            println("Lost ${healthLoss} health")
        }
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose ${scoreLoss} score, on activation you lose ${healthLoss} health")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Trap Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
        }
        return cardIds
    }

    fun addScoreGainerTestCard(amount: Int = 1): MutableList<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        val pointsPerCard = 4
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            val activationComponent = ActivationCounterComponent()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points")
            cardEntity add EffectComponent(onPlay = {
                addPassiveScoreGainerToThePlayer(pointsPerCard)
            })
            cardEntity add activationComponent
            cardEntity add NameComponent("Score Gainer Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
        }
        return cardIds
    }


    private fun addPassiveScoreGainerToThePlayer(amount: Int = 3) {

        val entity = generateEntity()

        val activationComponent = ActivationCounterComponent()
        val activateAction = { id: Int ->
            val playerScoreComponent = id get ScoreComponent::class
            playerScoreComponent.score.intValue += amount
            activationComponent.activate()
        }
        entity add activationComponent


        entity add EffectComponent(onTurnStart = activateAction)

        val effStackComp = (getPlayerID() get EffectStackComponent::class)
        effStackComp addEntity (entity)  // I think i have gone mad from the power
    }

}