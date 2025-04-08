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
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.activate
import com.sq.thed_ck_licker.ecs.components.addEntity
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
    val cards = Cards()
    private val componentManager = ComponentManager.componentManager


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
//        addDecayCardTest()
//        addPassiveScoreGainerToThePlayer()
//        addScoreGainerTestCard()
        addDeactivationTestCards()
    }

    // TODO: All these card things should come from
    //  some kind combination of card manager and card system
    private fun addDefaultCards(amount: Int = 7) {
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            componentManager.addComponent(cardEntity, ImageComponent())
            componentManager.addComponent(cardEntity, ScoreComponent(10 * i))
            componentManager.addComponent(
                cardEntity, DescriptionComponent()
            )
            componentManager.addComponent(cardEntity, NameComponent("Default Card #$i"))
            componentManager.addComponent(cardEntity, TagsComponent(listOf(CardTag.Card)))

            cardEntity add ActivationCounterComponent()
        }
    }

    private fun addDeactivationTestCards(amount: Int = 1) {
        val omaScore = ScoreComponent()
        val deactivateAction = { id: Int ->
            println("aa?")
            val target = id get HealthComponent::class
            omaScore.score.intValue += 1
            target.health.floatValue -= omaScore.score.intValue
            println("Now its deactivaited")
            println("Risk is rising!")
            println("Holds ${omaScore.score.intValue} points")
        }
        val onActivation = { id: Int ->
            println("aa?")
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

    private fun addDecayTestCards(amount: Int = 4) {
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent()
            cardEntity add EffectComponent()
            cardEntity add NameComponent("Decay Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.Card))
        }
    }

    private fun addScoreGainerTestCard(amount: Int = 1) {
        val pointsPerCard = 4
        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent("Gain Score gainer on play. \nEvery time you play card you gain $pointsPerCard points")
            cardEntity add EffectComponent(onPlay = { addPassiveScoreGainerToThePlayer(pointsPerCard) })
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
        val eka = componentManager.getEntitiesWithTags(listOf(CardTag.Card))?.keys
        return eka?.asSequence() ?: sequenceOf(2, 3, 4)
    }


    fun initTheGame() {
        initPlayerAndSomeDefaultThings()
    }

    fun getPlayerScoreM(): MutableIntState {
        val player = playerId()
        val comp = componentManager.getComponent(player, ScoreComponent::class)
        return comp.score
    }

}




