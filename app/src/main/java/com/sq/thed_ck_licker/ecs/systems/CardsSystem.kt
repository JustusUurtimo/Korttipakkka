package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.R
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
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.activate
import com.sq.thed_ck_licker.ecs.components.addEntity
import com.sq.thed_ck_licker.ecs.generateEntity
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlin.math.min
import kotlin.random.Random

class CardsSystem private constructor(private val componentManager: ComponentManager) {


    companion object {
        val instance: CardsSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardsSystem(ComponentManager.componentManager)
        }
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
            cardEntity add ActivationCounterComponent()
            cardIds.add(cardEntity)
        }
        return cardIds
    }

    // Function to pull a random card from deck
    fun pullRandomCardFromEntityDeck(entityId: Int) : Int {
        val drawDeck = componentManager.getComponent(entityId, DrawDeckComponent::class)
        check(drawDeck.drawCardDeck.isNotEmpty()) { "No cards available" }
        return drawDeck.drawCardDeck.getRandomElement()
    }




    fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {

        playerCardCount.intValue += 1
        val latestCardId = latestCard.intValue

        try {
            (latestCardId get EffectComponent::class).onPlay.invoke(getPlayerID())
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


    /* TODO: multipliers and such can be nicely implemented with:
    *   Make each effect function that takes in the thing  to be multiplied.
    *   Then just pump all things through pipeline full of the multiplier functions.
    *   To get the pipeline just loop all things and collect the multiplier functions.
    *   Something like multiplier system.
    *   If one wants to be wild they can even put three lists as pipeline, one for additions, one for increases and one for multiplications
    *   Thou that would mean order of operation does not matter, which might be wanted.
    */

    fun addMerchantCards(amount: Int = 5): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            //I know this is duplicate code, but we have to unify deck building to a one system
            // its on issue #48 atm
            val onActivationHeal = { id: Int ->
                (id get HealthComponent::class).health.floatValue += 5f
            }
            val healingCards = initCards(
                5,
                R.drawable.heal_10,
                "This card heals you",
                "Heal",
                listOf(CardTag.CARD),
                cardComponent = EffectComponent(onPlay = onActivationHeal)
            )
            val scoreGainerCards = addScoreGainerTestCard()
            val maxHpCards = addMaxHpTrapCard()
            val allCards = emptyList<Int>() + healingCards + scoreGainerCards + maxHpCards + emptyList<Int>()

            cardEntity add DrawDeckComponent(allCards as MutableList<Int>)
            cardEntity add ImageComponent()
            cardEntity add HealthComponent(99999f)
            cardEntity add ActivationCounterComponent()
            val openMerchant = { id: Int ->
                val target = id get MerchantComponent::class
                target.merchantId.intValue = cardEntity
            }
            cardEntity add EffectComponent(onPlay = openMerchant)
            cardEntity add DescriptionComponent("Activate to access shop")
            cardEntity add NameComponent("Merchant #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
        }
        return cardIds.toList()
    }

    fun addMaxHpTrapCard(amount: Int = 5): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add HealthComponent(99999f)
            cardEntity add ActivationCounterComponent()
            val maxHpIt = { id: Int ->
                val playerHp = id get HealthComponent::class
                if (Random.Default.nextFloat() <= 0.2f) {
                    (cardEntity get HealthComponent::class).health.floatValue -= 99999f
                    playerHp.health.floatValue = (playerHp.health.floatValue.div(2))
                } else {
                    playerHp.maxHealth.floatValue += 10f
                }
            }
            cardEntity add EffectComponent(onPlay = maxHpIt)
            cardEntity add DescriptionComponent("Gain 10 max health on play, might explode")
            cardEntity add NameComponent("Max HP Trap Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
        }
        return cardIds.toList()
    }

    fun addBreakingDefaultCards(amount: Int = 7): List<EntityId> {
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

            cardEntity add ActivationCounterComponent()
            val scoreIt = { id: Int ->
                val target = id get ScoreComponent::class
                target.score.intValue += omaScore.score.intValue
            }

            cardEntity add EffectComponent(onPlay = scoreIt)
            cardEntity add HealthComponent(10f)
        }
        return cardIds.toList()
    }

    fun addDeactivationTestCards(amount: Int = 2): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()

        val riskPoints = ScoreComponent()
        val deactivateAction = { id: Int ->
            val target = id get HealthComponent::class
            riskPoints.score.intValue += 1
            target.health.floatValue -= riskPoints.score.intValue.toFloat()
            println("Now its deactivated")
            println("Risk is rising!")
            println("Holds ${riskPoints.score.intValue} points")

        }
        val onActivation = { id: Int ->
            val target = id get ScoreComponent::class
            val scoreIncrease = riskPoints.score.intValue * 3
            target.score.intValue += (scoreIncrease)
            riskPoints.score.intValue = 0
            println("Now its activated")
            println("Gave $scoreIncrease points")
        }
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose health, on activation you gain score * 3")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Deactivation Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
            cardEntity add riskPoints
            cardEntity add ActivationCounterComponent()
        }
        return cardIds.toList()
    }

    fun addTrapTestCard(amount: Int = 2): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()

        var scoreLoss = 0
        var healthLoss = 0
        val activationComponent = ActivationCounterComponent()
        val deactivateAction = { id: Int ->
            val target = id get ScoreComponent::class
            scoreLoss = ((1 + activationComponent.deactivations.intValue) * 3)
            target.score.intValue -= (activationComponent.deactivations.intValue * 3)
            println("Lost $scoreLoss points")
        }
        val onActivation = { id: Int ->
            val target = id get HealthComponent::class
            healthLoss = activationComponent.activations.intValue + 1
            target.health.floatValue += activationComponent.activations.intValue

            println("Lost $healthLoss health")
        }
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose $scoreLoss score, on activation you lose $healthLoss health")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Trap Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
        }
        return cardIds.toList()
    }

    fun addScoreGainerTestCard(amount: Int = 1): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        val pointsPerCard = 3
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            val activationComponent = ActivationCounterComponent()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points")
            cardEntity add EffectComponent(onPlay = {
                addPassiveScoreGainerToThePlayer(pointsPerCard + i)
            })
            cardEntity add activationComponent
            cardEntity add NameComponent("Score Gainer Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
            cardEntity add HealthComponent(1f)
        }
        return cardIds.toList()
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


    fun addBeerGogglesTestCard(amount: Int = 1): List<EntityId> {
        val cardIds: MutableList<EntityId> = mutableListOf()
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardIds.add(cardEntity)
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("Equip Beer Goggles that will heal you bit.")
            cardEntity add EffectComponent(onPlay = {
                addLimitedSupplyAutoHealToThePlayer()
            })
            cardEntity add ActivationCounterComponent()
            cardEntity add NameComponent("Beer Goggles Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.CARD))
            cardEntity add HealthComponent(1f)
        }
        return cardIds.toList()
    }

    private fun addLimitedSupplyAutoHealToThePlayer(amount: Float = 150f) {
        val entity = generateEntity()
        val selfHp = HealthComponent(amount)
        entity add selfHp
        val selfActCounter = ActivationCounterComponent()
        entity add selfActCounter

        entity add EffectComponent(onTurnStart = { id: Int ->
            val targetHealthComponent = id get HealthComponent::class
            val targetMaxHp = targetHealthComponent.maxHealth.floatValue
            val targetHp = targetHealthComponent.health.floatValue
            if (targetHp < targetMaxHp / 2) {
                val healAmount = (targetMaxHp * 0.8f) - targetHp
                val ammm = min(selfHp.health.floatValue, healAmount)
                selfHp.health.floatValue -= ammm
                targetHealthComponent.health.floatValue += ammm
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.health.floatValue} health \nand have been activated ${selfActCounter.activations.intValue} times")
        })

        val effStackComp = (getPlayerID() get EffectStackComponent::class)
        effStackComp addEntity (entity)


    }

}