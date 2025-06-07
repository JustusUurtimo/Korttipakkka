package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.MyRandom.random
import jakarta.inject.Inject
import kotlin.math.abs

class CardCreationSystem @Inject constructor(
    private val cardsSystem: CardsSystem,
    private val cardBuilder: CardBuilderSystem
) {

    fun addShovelCards(amount: Int): List<EntityId> {

        val onActivation: (Int) -> Unit = { _ ->
            GameEvents.tryEmit(GameEvent.ShovelUsed)
        }

        return cardBuilder.buildCards {
            cardHealth = 10f
            cardAmount = amount
            description = "This card digs a hole"
            name = "Shovel"
            onCardPlay = onActivation
        }
    }

    fun addBasicScoreCards(amount: Int): List<EntityId> {
        val score = 10
        val onActivation = { targetId: Int ->
            (targetId get ScoreComponent::class).addScore(score)
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
        val onActivation = { playerId: Int ->
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
        val onActivation = { targetId: Int ->
            (targetId get HealthComponent::class).damage(2000f)
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
        val openMerchant = { _: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            MerchantEvents.tryEmit(MerchantEvent.MerchantShopOpened(merchantId, cardEntity))
        }

        return cardBuilder.buildCards {
            cardAmount = amount
            description = "Activate to access shop"
            name = "Merchant #$merchantId"
            characterId = merchantId
            onCardPlay = openMerchant
            tags = listOf(CardTag.CARD, CardTag.MERCHANT)
        }

    }

    fun addMaxHpTrapCards(amount: Int = 5): List<EntityId> {
        val cardHealthAmount = 100f
        val onActivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val targetHp = targetId get HealthComponent::class
            if (MyRandom.getRandomInt() <= 10) {
                (cardEntity get HealthComponent::class).damage(cardHealthAmount)
                val damageAmount = abs(targetHp.getHealth().div(2))
                targetHp.damage(damageAmount)
            } else {
                targetHp.increaseMaxHealth(10f)
            }
        }

        return cardBuilder.buildCards {
            cardHealth = cardHealthAmount
            cardAmount = amount
            description = "Gain 10 max health on play, might explode"
            name = "Max HP Trap Card"
            onCardPlay = onActivation
        }
    }


    fun addBreakingDefaultCards(amount: Int = 7): List<EntityId> {
        val scoreIt = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val target = targetId get ScoreComponent::class
            val omaScore = cardEntity get ScoreComponent::class
            target.addScore(omaScore.getScore())
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

        val onActivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val target = targetId get ScoreComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            val scoreIncrease = riskPoints.getScore() * 3
            target.addScore(scoreIncrease)
            riskPoints.setScore(0)
            println("Now its activated")
            println("Gave $scoreIncrease points")
        }
        val deactivateAction = { playerId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val target = playerId get HealthComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            riskPoints.addScore(1)
            target.damage(riskPoints.getScore().toFloat())
            println("Now its deactivated")
            println("Risk is rising!")
            println("Holds ${riskPoints.getScore()} points")

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

        val onDeactivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val target = targetId get ScoreComponent::class
            val activationComponent = (cardEntity get ActivationCounterComponent::class)
            target.reduceScore((activationComponent.getDeactivations() * 3))
        }
        val onActivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).latestCard
            val target = targetId get HealthComponent::class
            val activationComponent = cardEntity get ActivationCounterComponent::class
            val damageAmount = (activationComponent.getActivations() * 5).toFloat()
            target.damage(damageAmount)
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
        val onActivation = { playerId: Int ->
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
        val onActivation = { playerId: Int ->
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

    fun addTempMultiplierTestCards(amount: Int = 1): List<EntityId> {
        val onActivation = { targetId: Int ->
            cardsSystem.addTemporaryMultiplierTo(targetId)
        }

        return cardBuilder.buildCards {
            cardHealth = 1f
            cardAmount = amount
            description = "Inject steroids and make more every time you do any thing."
            name = "Steroids"
            onCardPlay = onActivation
        }
    }
    fun addShuffleTestCards(amount: Int = 1): List<EntityId> {
        val playerId = getPlayerID()

        val onActivation: (Int) -> Unit = { _: Int ->
            val playerDeck = playerId get DrawDeckComponent::class
            val playerDiscardDeck = playerId get DiscardDeckComponent::class
            val card = if (playerDeck.getDrawCardDeck().isEmpty()) {
                playerDiscardDeck.getDiscardDeck()
                    .removeAt(random.nextInt(playerDiscardDeck.getDiscardDeck().size))
            } else {
                playerDeck.getDrawCardDeck()
                    .removeAt(random.nextInt(playerDeck.getDrawCardDeck().size))
            }
            val effect = card get EffectComponent::class
            Log.i("Shuffle on activation", "Effect: $effect")
            val second = effect.shuffleToNew()
            Log.i("Shuffle on activation", "Second: $second")
            card add second
            playerDeck.getDrawCardDeck().add(card)
        }

        val onDeactivation: (Int) -> Unit = { _: Int ->
            val playerDeck = playerId get DrawDeckComponent::class
            val playerDiscardDeck = playerId get DiscardDeckComponent::class
            val card = if (playerDiscardDeck.getDiscardDeck().isEmpty()) {
                playerDeck.getDrawCardDeck()
                    .removeAt(random.nextInt(playerDeck.getDrawCardDeck().size))
            } else {
                playerDiscardDeck.getDiscardDeck()
                    .removeAt(random.nextInt(playerDiscardDeck.getDiscardDeck().size))
            }
            val effect = card get EffectComponent::class
            Log.i("Shuffle on deactivation", "Effect: $effect")
            val second = effect.shuffleToNew()
            Log.i("Shuffle on deactivation", "Second: $second")
            card add second
            playerDiscardDeck.getDiscardDeck().add(card)
        }

        return cardBuilder.buildCards {
            cardHealth = 20f
            cardAmount = amount
            description =
                "On Activation corrupt up to 1 card(s) in discard, On Deactivation corrupt up to 1 card(s) in draw deck"
            name = "Corrupt cards"
            onCardPlay = onActivation
            onCardDeactivate = onDeactivation
        }
    }
    fun addTimeBoundTestCards(numberOfCards: Int = 1): List<EntityId> {
        return cardBuilder.createTimeBoundCards(numberOfCards)
    }
}
