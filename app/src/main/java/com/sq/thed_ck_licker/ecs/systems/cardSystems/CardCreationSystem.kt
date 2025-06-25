package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.CardTag
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggerAndEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
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
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.addComp
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.generateCards
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withBasicCardDefaults
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withEffect
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withHealth
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withName
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem2.withScore
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems
import com.sq.thed_ck_licker.helpers.DescribedEffect
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.MyRandom.random
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen
import javax.inject.Inject
import kotlin.math.abs

class CardCreationSystem @Inject constructor(
    private val cardCreationHelperSystems: CardCreationHelperSystems,
    private val cardBuilder: CardBuilderSystem,
    private val gameNavigator: GameNavigator
) {

    fun addShovelCards(amount: Int): List<EntityId> {

        val onActivation: (Int) -> Unit = { _ ->
            GameEvents.tryEmit(GameEvent.ShovelUsed)
        }
        val describedEffect = DescribedEffect(onActivation) { "Open the fiery pit of doom!" }
        return cardBuilder.buildCards {
            cardHealth = 10f
            cardAmount = amount
            name = "Shovel"
            onCardPlay = describedEffect
        }
    }

    fun addBasicScoreCards(amount: Int): List<EntityId> {
        val score = 10
        val onActivation = { targetId: Int ->
            (targetId get ScoreComponent::class).addScore(score)
        }
        val describedEffect = DescribedEffect(onActivation) { "Gives $score points" }
        return cardBuilder.buildCards {
            cardHealth = 10f
            scoreAmount = score
            cardAmount = amount
            name = "Basic score card"
            onCardPlay = describedEffect
        }
    }

    fun addBasicScoreCardsV2(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults()(cardId)
            withHealth(100f)(cardId)
            withScore(10)(cardId)
            withName("Basic score card V2")(cardId)
            val describedEffect = DescribedEffect(action = { targetId ->
                val score = (cardId get ScoreComponent::class).getScore()
                (targetId get ScoreComponent::class).addScore(score)
            }) {
                val score = (cardId get ScoreComponent::class).getScore()
                "Gain $score points"
            }
            withEffect(EffectComponent(onPlay = describedEffect))(cardId)
        }
    }

    fun addBasicScoreCardsV3(amount: Int): List<EntityId> {
        return generateCards(amount) { cardId ->
            withBasicCardDefaults()(cardId)
            withHealth(100f)(cardId)
            withScore(10)(cardId)
            withName("Basic score card V3")(cardId)
            addComp{
                val score = (cardId get ScoreComponent::class).getScore()
                TriggerAndEffectsComponent(
                    trigger = Trigger.OnPlay,
                    effects = listOf(Effect.GainScore(score))
                )
            }(cardId)
        }
    }

    fun addHealingCards(amount: Int): List<EntityId> {
        val healAmount = 40f
        val onActivation = { playerId: Int ->
            (playerId get HealthComponent::class).heal(healAmount)
        }
        val describedEffect = DescribedEffect(onActivation) { "Heal ($healAmount)" }
        return cardBuilder.buildCards {
            cardHealth = 5f
            cardAmount = amount
            cardImage = R.drawable.heal_10
            name = "Heal"
            onCardPlay = describedEffect
        }
    }

    fun addDamageCards(amount: Int): List<EntityId> {
        val damageAmount = 150f
        val onActivation = { targetId: Int ->
            (targetId get HealthComponent::class).damage(damageAmount)
        }
        val describedEffect = DescribedEffect(onActivation) { "Take damage ($damageAmount)" }
        return cardBuilder.buildCards {
            cardAmount = amount
            cardImage = R.drawable.damage_6
            name = "Damage"
            onCardPlay = describedEffect
        }
    }

    fun addMerchantCards(amount: Int, merchantId: Int): List<EntityId> {
        val openMerchant = { _: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            MerchantEvents.tryEmit(MerchantEvent.MerchantShopOpened(merchantId, cardEntity))
            gameNavigator.navigateTo(Screen.MerchantShop.route)
        }
        val describedEffect = DescribedEffect(openMerchant) { "Gain access to a shop" }
        return cardBuilder.buildCards {
            cardAmount = amount
            name = "Merchant #$merchantId"
            characterId = merchantId
            onCardPlay = describedEffect
            tags = listOf(CardTag.CARD, CardTag.MERCHANT)
        }

    }

    fun addMaxHpTrapCards(amount: Int = 5): List<EntityId> {
        val cardHealthAmount = 100f
        val maxHp = 10f
        val onActivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            val targetHp = targetId get HealthComponent::class
            if (MyRandom.getRandomInt() <= 1) {
                (cardEntity get HealthComponent::class).damage(cardHealthAmount)
                val damageAmount = abs(targetHp.getHealth().div(2))
                targetHp.damage(damageAmount)
            } else {
                targetHp.increaseMaxHealth(maxHp)
            }
        }
        val describedEffect = DescribedEffect(onActivation) { "Gain $maxHp max health on play, might explode" }
        return cardBuilder.buildCards {
            cardHealth = cardHealthAmount
            cardAmount = amount
            name = "Max HP Trap Card"
            onCardPlay = describedEffect
        }
    }


    fun addBreakingDefaultCards(amount: Int = 7): List<EntityId> {
        val scoreIt = { targetId: Int ->
            val cardEntity = (targetId get LatestCardComponent::class).getLatestCard()
            val target = targetId get ScoreComponent::class
            val omaScore = cardEntity get ScoreComponent::class
            target.addScore(omaScore.getScore())
        }
        val describedEffect = DescribedEffect(scoreIt) { targetId: Int ->
            val cardEntity = (targetId get LatestCardComponent::class).getLatestCard()
            val omaScore = cardEntity get ScoreComponent::class
            "Gain ${omaScore.getScore()} points"
        }

        val cards = cardBuilder.buildCards {
            cardHealth = 10f
            scoreAmount = 100
            cardAmount = amount
            name = "Default Card"
            onCardPlay = describedEffect
        }
        return cards
    }


    fun addDeactivationTestCards(amount: Int = 2): List<EntityId> {
        var stepSize = 2
        var baseDamage = 1
        val onActivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            val target = targetId get ScoreComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            val scoreIncrease = riskPoints.getScore() * 30
            target.addScore(scoreIncrease)
            riskPoints.setScore(0)
        }
        val deactivateAction = { playerId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            val target = playerId get HealthComponent::class
            val riskPoints = cardEntity get ScoreComponent::class
            riskPoints.addScore(baseDamage)
            stepSize++
            baseDamage += stepSize
            target.damage(riskPoints.getScore().toFloat())

        }
        val activationEffect = DescribedEffect(onActivation) { targetId ->
            val score = (targetId get ScoreComponent::class).getScore()
            "Gain 3x points based on health lost from this card ($score points)"
        }
        val deactivationEffect = DescribedEffect(deactivateAction) { _ ->
            "Lose health ($baseDamage points)"
        }
        return cardBuilder.buildCards {
            scoreAmount = 0
            cardAmount = amount
            name = "Deactivation Card"
            onCardPlay = activationEffect
            onCardDeactivate = deactivationEffect
        }
    }

    fun addTrapTestCards(amount: Int = 1): List<EntityId> {


        val onDeactivation = { targetId: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            val target = targetId get ScoreComponent::class
            val activationComponent = (cardEntity get ActivationCounterComponent::class)
            target.reduceScore(((1 + activationComponent.getDeactivations()) * 30))
        }
        val deactivationEffect =
            DescribedEffect(onDeactivation) { targetId: EntityId ->
                val cardEntity = (targetId get LatestCardComponent::class).getLatestCard()
                val activationComponent = (cardEntity get ActivationCounterComponent::class)
                val amount = (1 + activationComponent.getDeactivations()) * 30
                "Lose Score based on deactivations ($amount score)"
            }


        val onActivation = { targetId: Int ->
            val cardEntity = (targetId get LatestCardComponent::class).getLatestCard()
            val target = targetId get HealthComponent::class
            val activationComponent = cardEntity get ActivationCounterComponent::class
            val damageAmount = ((1 + activationComponent.getActivations()) * 5).toFloat()
            target.damage(damageAmount)
        }
        val activationEffect = DescribedEffect(onActivation) { _: Int ->
            val cardEntity = (getPlayerID() get LatestCardComponent::class).getLatestCard()
            val activationComponent = cardEntity get ActivationCounterComponent::class
            val damageAmount = ((1 + activationComponent.getActivations()) * 5).toFloat()
            "Lose health based on activations (${damageAmount} health)"
        }


        return cardBuilder.buildCards {
            scoreAmount = 1
            cardAmount = amount
            name = "Trap card"
            onCardPlay = activationEffect
            onCardDeactivate = deactivationEffect
        }
    }


    fun addScoreGainerTestCards(amount: Int = 1): List<EntityId> {
        val pointsPerCard = 3
        val onActivation = { playerId: Int ->
            cardCreationHelperSystems.addPassiveScoreGainerToEntity(playerId, pointsPerCard)
        }
        val activationEffect = DescribedEffect(onActivation) { "Gain Score gainer\n Every time you play card you gain $pointsPerCard points" }
        return cardBuilder.buildCards {
            cardHealth = 1f
            scoreAmount = pointsPerCard
            cardAmount = amount
            name = "Score Gainer Card"
            onCardPlay = activationEffect
        }
    }

    fun addBeerGogglesTestCards(amount: Int = 1): List<EntityId> {
        val healLimit = 150f
        val onActivation = { playerId: Int ->
            cardCreationHelperSystems.addLimitedSupplyAutoHealToEntity(playerId, healLimit)
        }
        val activationEffect = DescribedEffect(onActivation) { "Equip Beer Goggles that will heal you bit (up to $healLimit health points)" }
        return cardBuilder.buildCards {
            cardHealth = 1f
            cardAmount = amount
            name = "Beer Goggles Card"
            onCardPlay = activationEffect
        }
    }

    fun addTempMultiplierTestCards(amount: Int = 1): List<EntityId> {
        val multiplier = 3f
        val onActivation = { targetId: Int ->
            cardCreationHelperSystems.addTemporaryMultiplierTo(
                targetEntityId = targetId,
                multiplier = multiplier
            )
        }
        val activationEffect = DescribedEffect(onActivation) { "Inject steroids and make more every time you do any thing ($multiplier times)" }
        return cardBuilder.buildCards {
            cardHealth = 1f
            cardAmount = amount
            name = "Steroids"
            onCardPlay = activationEffect
        }
    }

    fun addShuffleTestCards(amount: Int = 1, efficiency: Int = 1): List<EntityId> {
        val playerId = getPlayerID()

        val onActivation: (Int) -> Unit = { _: Int ->
            val playerDeck = playerId get DrawDeckComponent::class
            val playerDiscardDeck = playerId get DiscardDeckComponent::class
            repeat(efficiency) {
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
            (card get TagsComponent::class).addTag(CardTag.CORRUPTED)
            playerDeck.getDrawCardDeck().add(card)
            }
        }

        val onDeactivation: (Int) -> Unit = { _: Int ->
            val playerDeck = playerId get DrawDeckComponent::class
            val playerDiscardDeck = playerId get DiscardDeckComponent::class
            repeat(efficiency) {
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
            (card get TagsComponent::class).addTag(CardTag.CORRUPTED)
                playerDiscardDeck.getDiscardDeck().add(card)
            }
        }
        val activationEffect = DescribedEffect(onActivation) { "Corrupt $efficiency card(s) in discard" }
        val deactivationEffect =
            DescribedEffect(onDeactivation) { "Corrupt $efficiency card(s) in draw deck" }
        return cardBuilder.buildCards {
            cardHealth = 20f
            cardAmount = amount
            name = "Corrupt cards"
            onCardPlay = activationEffect
            onCardDeactivate = deactivationEffect
        }
    }

    fun addTimeBoundTestCards(numberOfCards: Int = 1): List<EntityId> {
        return cardBuilder.createTimeBoundCards(numberOfCards)
    }
}
