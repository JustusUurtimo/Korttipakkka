package com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DeckHelper
import com.sq.thed_ck_licker.helpers.getRandomElement

/**
 * This can be used to do others activations.
 * Currently it does not count as activating it, so they do not lose health from it.
 * Unless they have specific effects for that.
 *
 * @param newSource The entity to activate
 * @param asTrigger The trigger to use
 *
 */
data class CoActivation(
    val newSource: EntityId?,
    val asTrigger: Trigger?,
    var isThisRecursion: Boolean = false
) : MiscEffect() {
    override fun describe(): String {
        return "Co-activate entity $newSource with $asTrigger"
    }

    override fun execute(context: EffectContext): Float {
        /*
         * You can still hit infinite recursion with this.
         * While unlikely, if this hits another coactivation and that hits this back, they will recurse forever.
         * If this becomes actual concern, we can just make the recursion lock be global.
         */
        if (this.isThisRecursion == true) return -1f
        this.isThisRecursion = true
        Log.i("coactivate", "CoActivation starts")

        val newSource = if (this.newSource != null) {
            this.newSource
        } else {
            val deck = DeckHelper.getEntityFullDeck(context.target)
            deck.getRandomElement()
        }
        val newTrigger = if (this.asTrigger != null) {
            this.asTrigger
        } else {
            val trigEffComp = newSource get TriggeredEffectsComponent::class
            trigEffComp.effectsByTrigger.keys.toList().getRandomElement()
        }

        TriggerEffectHandler.handleTriggerEffect(
            EffectContext(
                trigger = newTrigger,
                source = newSource,
                target = context.target,
            )
        )
        Log.i("coactivate", "CoActivation ends")
        this.isThisRecursion = false
        return 1f
    }
}
