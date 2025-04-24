package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem.Companion.instance as cardsSystem


class CardCreationSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {

    companion object {
        val instance: CardCreationSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardCreationSystem(ComponentManager.componentManager)
        }
    }

    fun addBasicScoreCards(amount: Int): List<EntityId> {
        val scoreAmount = 10
        val onActivation = { targetId: Int, _: Int ->
            (targetId get ScoreComponent::class).score.intValue += scoreAmount
        }

        return cardsSystem.initCards(
            cardHealth = 10f,
            scoreAmount,
            amount,
            R.drawable.placeholder,
            description = "Gives small amount of score",
            name = "Basic score card",
            tags = listOf(CardTag.CARD),
            onActivation
        )
    }

    fun addHealingCards(amount: Int): List<EntityId> {
        val onActivation = { playerId: Int, _: Int ->
            (playerId get HealthComponent::class).heal(10f)
        }

        return cardsSystem.initCards(
            null,
            null,
            amount,
            R.drawable.heal_10,
            "This card heals you",
            "Heal",
            listOf(CardTag.CARD),
            onActivation
        )
    }

    fun addDamageCards(amount: Int): List<EntityId> {
        val onActivation = { targetId: Int, _: Int ->
            (targetId get HealthComponent::class).health -= 5f
        }

        return cardsSystem.initCards(
            null,
            null,
            amount,
            R.drawable.damage_6,
            "This card deals damage to you",
            "Damage",
            listOf(CardTag.CARD),
            onActivation
        )
    }

    fun addMerchantCards(amount: Int, merchantId: Int): List<EntityId> {
        val openMerchant = { targetId: Int, cardEntity: Int ->
            val target = targetId get MerchantComponent::class
            target.merchantId.intValue = merchantId
            target.activeMerchantSummonCard.intValue = cardEntity
        }

        return cardsSystem.initCards(
            null,
            null,
            amount,
            R.drawable.placeholder,
            "Activate to access shop",
            "Merchant #$merchantId",
            listOf(CardTag.CARD),
            openMerchant,
        )

    }

    fun addMaxHpTrapCards(amount: Int = 5): List<EntityId> {
        val maxHpIt = { targetId: Int, cardEntity: Int ->
            val targetHp = targetId get HealthComponent::class
            if (MyRandom.getRandomInt() <= 2) {
                (cardEntity get HealthComponent::class).health -= 99999f
                targetHp.health = (targetHp.health.div(2))
            } else {
                targetHp.maxHealth += 10f
            }
        }
        return cardsSystem.initCards(
            99999f,
            null,
            amount,
            R.drawable.placeholder,
            "Gain 10 max health on play, might explode",
            "Max HP Trap Card",
            listOf(CardTag.CARD),
            maxHpIt,
        )
    }


    fun addBreakingDefaultCards(amount: Int = 7): List<EntityId> {
        val scoreIt = { targetId: Int, cardEntity: Int ->
            val target = targetId get ScoreComponent::class
            val omaScore = cardEntity get ScoreComponent::class
            target.score.intValue += omaScore.score.intValue
        }
        return cardsSystem.initCards(
            10f,
            100,
            amount,
            R.drawable.placeholder,
            "",
            "Default Card",
            listOf(CardTag.CARD),
            scoreIt
        )
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
            target.health -= riskPoints.score.intValue.toFloat()
            println("Now its deactivated")
            println("Risk is rising!")
            println("Holds ${riskPoints.score.intValue} points")

        }
        return cardsSystem.initCards(
            null,
            0,
            amount,
            R.drawable.placeholder,
            "On deactivate you lose health, on activation you gain score * 3",
            "Deactivation Card ",
            listOf(CardTag.CARD),
            onActivation,
            deactivateAction
        )
    }

    fun addTrapTestCard(amount: Int = 2): List<EntityId> {

        val onDeactivation = { targetId: Int, entityId: Int ->
            val target = targetId get ScoreComponent::class
            val activationComponent = entityId get ActivationCounterComponent::class
            target.score.intValue -= (activationComponent.deactivations.intValue * 3)
        }
        val onActivation = { targetId: Int, entityId: Int ->
            val target = targetId get HealthComponent::class
            val activationComponent = entityId get ActivationCounterComponent::class
            target.health -= (activationComponent.activations.intValue * 5)
        }

        return cardsSystem.initCards(
            null,
            1,
            amount,
            R.drawable.placeholder,
            "On deactivate you lose score, on activation you lose health",
            "Trap card",
            listOf(CardTag.CARD),
            onActivation,
            onDeactivation
        )
    }


    fun addScoreGainerTestCard(amount: Int = 1): List<EntityId> {
        val pointsPerCard = 3
        val onActivation = { playerId: Int, _: Int ->
            cardsSystem.addPassiveScoreGainerToEntity(playerId, pointsPerCard)
        }
        return cardsSystem.initCards(
            1f,
            3,
            amount,
            R.drawable.placeholder,
            "Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points\"",
            "Score Gainer Card",
            listOf(CardTag.CARD),
            onActivation
        )
    }

    fun addBeerGogglesTestCard(amount: Int = 1): List<EntityId> {
        val onActivation = { playerId: Int, _: Int ->
            cardsSystem.addLimitedSupplyAutoHealToEntity(playerId, 150f)
        }
        return cardsSystem.initCards(
            1f,
            null,
            amount,
            R.drawable.placeholder,
            "Equip Beer Goggles that will heal you bit.",
            "Beer Goggles Card",
            listOf(CardTag.CARD),
            onActivation
        )
    }


}