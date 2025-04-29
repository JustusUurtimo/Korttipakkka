package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.damage
import com.sq.thed_ck_licker.ecs.components.heal
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.MyRandom
import jakarta.inject.Inject

class CardCreationSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val cardEffectSystem: CardEffectSystem,
    private val cardBuilder: CardBuilderSystem
) {

    fun addBasicScoreCards(amount: Int): List<EntityId> {
        val score = 10
        val onActivation = { targetId: Int, _: Int ->
            (targetId get ScoreComponent::class).score.intValue += score
        }

        return cardBuilder.buildCards {
            cardHealth = 10f
            scoreAmount = score
            cardAmount = amount
            description = "Gives small amount of score"
            name = "Basic score card"
            onCardPlay = onActivation
        }
    }

    fun addHealingCards(amount: Int): List<EntityId> {
        val onActivation = { playerId: Int, _: Int ->
            (playerId get HealthComponent::class).heal(10f)
        }

        return cardBuilder.buildCards {
            cardAmount = amount
            cardImage = R.drawable.heal_10
            description = "This card heals you"
            name = "Heal"
            onCardPlay = onActivation
        }
    }

    fun addDamageCards(amount: Int): List<EntityId> {
        val onActivation = { targetId: Int, _: Int ->
            (targetId get HealthComponent::class).damage(2000f)
            cardEffectSystem.onHealthChanged(targetId)
        }

        return cardBuilder.buildCards {
            cardAmount = amount
            cardImage = R.drawable.damage_6
            description = "This card deals damage to you"
            name = "Damage"
            onCardPlay = onActivation
        }
    }

    fun addMerchantCards(amount: Int, merchantId: Int): List<EntityId> {
        val openMerchant = { targetId: Int, cardEntity: Int ->
            val target = targetId get MerchantComponent::class
            target.merchantId.intValue = merchantId
            target.activeMerchantSummonCard.intValue = cardEntity
        }

        return cardBuilder.buildCards {
            cardAmount = amount
            description = "Activate to access shop"
            name = "Merchant #$merchantId"
            onCardPlay = openMerchant
        }

    }

    fun addMaxHpTrapCards(amount: Int = 5): List<EntityId> {
        val onActivation = { targetId: Int, cardEntity: Int ->
            val targetHp = targetId get HealthComponent::class
            if (MyRandom.getRandomInt() <= 2) {
                (cardEntity get HealthComponent::class).health.floatValue -= 99999f
                targetHp.health.floatValue = (targetHp.health.floatValue.div(2))
            } else {
                targetHp.maxHealth.floatValue += 10f
            }
        }

        return cardBuilder.buildCards {
            cardHealth = 99999f
            cardAmount = amount
            description = "Gain 10 max health on play, might explode"
            name = "Max HP Trap Card"
            onCardPlay = onActivation
        }
    }


    fun addBreakingDefaultCards(amount: Int = 7): List<EntityId> {
        val scoreIt = { targetId: Int, cardEntity: Int ->
            val target = targetId get ScoreComponent::class
            val omaScore = cardEntity get ScoreComponent::class
            target.score.intValue += omaScore.score.intValue
        }

        return cardBuilder.buildCards {
            cardHealth = 10f
            scoreAmount = 100
            cardAmount = amount
            name = "Default Card"
            onCardPlay = scoreIt
        }
    }


    fun addDeactivationTestCards(amount: Int = 2): List<EntityId> {

        val onActivation = { targetId: Int, cardEntity: Int ->
            val target = targetId get ScoreComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            val scoreIncrease = riskPoints.score.intValue * 3
            target.score.intValue += (scoreIncrease)
            riskPoints.score.intValue = 0
            println("Now its activated")
            println("Gave $scoreIncrease points")
        }
        val deactivateAction = { playerId: Int, cardEntity: Int ->
            val target = playerId get HealthComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            riskPoints.score.intValue += 1
            target.health.floatValue -= riskPoints.score.intValue.toFloat()
            println("Now its deactivated")
            println("Risk is rising!")
            println("Holds ${riskPoints.score.intValue} points")

        }

        return cardBuilder.buildCards {
            scoreAmount = 0
            cardAmount = amount
            description = "On deactivate you lose health, on activation you gain score * 3"
            name = "Deactivation Card "
            onCardPlay = onActivation
            onCardDeactivate = deactivateAction
        }
    }

    fun addTrapTestCards(amount: Int = 1): List<EntityId> {

        val onDeactivation = { targetId: Int, entityId: Int ->
            val target = targetId get ScoreComponent::class
            val activationComponent = entityId get ActivationCounterComponent::class
            target.score.intValue -= (activationComponent.deactivations.intValue * 3)
        }
        val onActivation = { targetId: Int, entityId: Int ->
            val target = targetId get HealthComponent::class
            val activationComponent = entityId get ActivationCounterComponent::class
            target.health.floatValue -= (activationComponent.activations.intValue * 5)
        }

        return cardBuilder.buildCards {
            scoreAmount = 1
            cardAmount = amount
            description = "On deactivate you lose score, on activation you lose health"
            name = "Trap card"
            onCardPlay = onActivation
            onCardDeactivate = onDeactivation
        }
    }


    fun addScoreGainerTestCards(amount: Int = 1): List<EntityId> {
        val pointsPerCard = 3
        val onActivation = { playerId: Int, _: Int ->
            cardsSystem.addPassiveScoreGainerToEntity(playerId, pointsPerCard)
        }

        return cardBuilder.buildCards {
            cardHealth = 1f
            scoreAmount = 3
            cardAmount = amount
            description =
                "Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points\""
            name = "Score Gainer Card"
            onCardPlay = onActivation
        }
    }

    fun addBeerGogglesTestCards(amount: Int = 1): List<EntityId> {
        val onActivation = { playerId: Int, _: Int ->
            cardsSystem.addLimitedSupplyAutoHealToEntity(playerId, 150f)
        }

        return cardBuilder.buildCards {
            cardHealth = 1f
            cardAmount = amount
            description = "Equip Beer Goggles that will heal you bit."
            name = "Beer Goggles Card"
            onCardPlay = onActivation
        }
    }
}