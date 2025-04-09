package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.card.Cards
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.CardClassification
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.CardEffectValue
import com.sq.thed_ck_licker.ecs.components.CardIdentity
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.activate
import com.sq.thed_ck_licker.ecs.components.addEntity
import com.sq.thed_ck_licker.ecs.components.deactivate
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem
import com.sq.thed_ck_licker.ecs.systems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.PlayerSystem
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId

/*TODO apparently this kind a not good...
*  If you want I can do new refactor to make it better.
*  But I think this is good enough for now.
*  More about this https://developer.android.com/topic/architecture/ui-layer/stateholders
*  aa
*  Maybe this should be relegated for testing and prototyping
*  since the systems should handle all the things
 */
@Deprecated("Everything should go through their own systems any way\nThou there may be argument to put things here until they find their actual home")
object TheGameHandler {
    private val componentManager = ComponentManager.componentManager
    val cardsSystem = CardsSystem(componentManager)
    private val playerSystem = PlayerSystem(componentManager)
    private val descriptionSystem = DescriptionSystem(componentManager)
    val cardDisplaySystem = CardDisplaySystem(componentManager)
    val cardEffectSystem = CardEffectSystem(componentManager)



    fun getPlayerHealthM(): MutableFloatState {
        val player = playerId()
        val comp = componentManager.getComponent(player, HealthComponent::class)
        return comp.health
    }


//    fun getEntityHealth(entityId: Int): Float {
//        val comp = componentManager.getComponent(entityId, HealthComponent::class)
//        return comp.health.floatValue
//    }
//
//    fun getEntityHealthM(entityId: Int): MutableFloatState {
//        val comp = componentManager.getComponent(entityId, HealthComponent::class)
//        return comp.health
//    }

    // TODO this wont stay here
    /**
     * käytetään placeholderina kun ei ole vielä vedetty kortteja
     * Returns a default card pair with a placeholder card.
     */
    @Deprecated("Use addDefaultCards instead")
    fun getDefaultCardPair(): Pair<CardIdentity, CardEffect> {
        val defaultCardPair = Pair(
            CardIdentity(-1, R.drawable.card_back),
            CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_2)
        )
        return defaultCardPair
    }

    // TODO this should not stay here
    private fun initPlayerAndSomeDefaultThings() {
        componentManager.addComponent(playerId(), HealthComponent(0f, 100f))
        componentManager.addComponent(playerId(), ScoreComponent())
        componentManager.addComponent(playerId(), DrawDeckComponent())
        playerId() add EffectStackComponent()


        addDefaultCards()
        addTrapTestCard()
        addScoreGainerTestCard()
        addDeactivationTestCards()


//        addPassiveScoreGainerToThePlayer()

        /* TODO: multipliers and such can be nicely implemented with:
        *   Make each effect function that takes in the thing  to be multiplied.
        *   Then just pump all things through pipeline full of the multiplier functions.
        *   To get the pipeline just loop all things and collect the multiplier functions.
        *   Something like multiplier system.
        *   If one wants to be wild they can even put three lists as pipeline, one for additions, one for increases and one for multiplications
        *   Thou that would mean order of operation does not matter, which might be wanted.
        */
    }

    // TODO: All these card things should come from
    //  some kind combination of card manager and card system
    private fun addDefaultCards(amount: Int = 7) {
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            val omaScore = ScoreComponent(10 * i)
            componentManager.addComponent(cardEntity, ImageComponent())
            componentManager.addComponent(cardEntity, omaScore)
            componentManager.addComponent(
                cardEntity, DescriptionComponent()
            )
            componentManager.addComponent(cardEntity, NameComponent("Default Card #$i"))
            componentManager.addComponent(cardEntity, TagsComponent(listOf(CardTag.Card)))

            val selfAct = ActivationCounterComponent()
            cardEntity add selfAct
            val scoreIt = { id: Int ->
                val target = id get ScoreComponent::class
                target.score.intValue += omaScore.score.intValue
                selfAct.activate()
                //TODO: This certainly is not right way of doing this,
                // it should be handled in some general way, maybe some event based thing or system for them
            }

            cardEntity add EffectComponent(onPlay = scoreIt)

        }
    }

    private fun addDeactivationTestCards(amount: Int = 2) {
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
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose health, on activation you gain score * 3")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Deactivation Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.Card))
            cardEntity add omaScore
        }
    }

    private fun addTrapTestCard(amount: Int = 2) {
        var scoreLoss = 0
        var healthLoss = 0
        val activationComponent = ActivationCounterComponent()
        val deactivateAction = { id: Int ->
            val target = id get ScoreComponent::class
            scoreLoss = ((1 + activationComponent.deactivations.intValue) * 3)
            target.score.intValue -= (activationComponent.deactivations.intValue * 3)
            activationComponent.deactivate()
            println("Lost ${scoreLoss} points")
        }
        val onActivation = { id: Int ->
            val target = id get HealthComponent::class
            healthLoss = activationComponent.activations.intValue + 1
            target.health.floatValue += activationComponent.activations.intValue
            activationComponent.activate()

            println("Lost ${healthLoss} health")
        }
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("On deactivate you lose ${scoreLoss} score, on activation you lose ${healthLoss} health")
            cardEntity add EffectComponent(onDeactivate = deactivateAction, onPlay = onActivation)
            cardEntity add NameComponent("Trap Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.Card))
        }
    }

    private fun addScoreGainerTestCard(amount: Int = 1) {
        val pointsPerCard = 4
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            val activationComponent = ActivationCounterComponent()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points")
            cardEntity add EffectComponent(onPlay = {
                addPassiveScoreGainerToThePlayer(pointsPerCard)
                activationComponent.activate()
            })
            cardEntity add activationComponent
            cardEntity add NameComponent("Score Gainer Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.Card))
        }
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
        println("effStackComp #2: $effStackComp")
    }

    // TODO this is just temporary
    //  it is meant only for brief debugging thing
    //  it is really baad
    @Deprecated("It is so baad that it should be already deprecated")
    fun getRandomCard(): Map<Int, Any>? {
        return componentManager.getEntitiesWithTags(listOf(CardTag.Card))
    }

    fun getTestingCardSequence(): Sequence<Int> {
        val eka = componentManager.getEntitiesWithTags(listOf(CardTag.CARD)).keys
        return eka.asSequence()
    }

    fun getPlayerScoreM(): MutableIntState {
        val player = playerId()
        val comp = componentManager.getComponent(player, ScoreComponent::class)
        return comp.score
    }

}