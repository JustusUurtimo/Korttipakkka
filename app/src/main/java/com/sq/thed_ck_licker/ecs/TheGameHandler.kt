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
import com.sq.thed_ck_licker.ecs.components.DurationComponent
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
        addDecayCardTest()
        addPassiveScoreGainerToThePlayer()
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

    private fun addDecayCardTest(amount: Int = 4) {

        for (i in 1..amount) {
            val cardEntity = generateEntity()
            cardEntity add ImageComponent()
            cardEntity add DescriptionComponent()
            cardEntity add EffectComponent()
            cardEntity add NameComponent("Decay Card #$i")
            cardEntity add TagsComponent(listOf(CardTag.Card))
        }
    }

    private fun addPassiveScoreGainerToThePlayer(amount: Int = 4) {

        val entity = generateEntity()
//        entity add TagsComponent(listOf(CardTag.Effect))
        entity add DurationComponent()


        val activationComponent = ActivationCounterComponent()
        val funkkari = { id: Int ->
            val playerScoreComponent = id get ScoreComponent::class
            playerScoreComponent.score.intValue += amount
            activationComponent.activate()
            println("Now it should be done adding")
        }
        entity add activationComponent


        entity add EffectComponent(onTurnStart = funkkari)

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


fun onTurnStartEffectStackSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val targetsWithEffectStack = componentManager.getEntitiesWithComponent(EffectStackComponent::class)
    if (targetsWithEffectStack == null) return

    for (effectTarget in targetsWithEffectStack) {
        val effectStack = effectTarget.value as EffectStackComponent
        for (effectEntity in effectStack.effectEntities) {
                val effect = effectEntity get EffectComponent::class
                effect.onTurnStart(effectTarget.key)
            println("Activating ${effect} on ${effectTarget.key}")
        }
        println("effectTarget.key: ${effectTarget.key}")
        println("effectTarget.value: ${effectTarget.value}")
//        val theStacker = effectTarget get EffectStackComponent::class
    }
}


