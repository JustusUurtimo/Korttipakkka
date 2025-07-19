package com.sq.thed_ck_licker.ecs.components.effectthing

abstract class Trigger {
    override fun toString(): String {
        return this.javaClass.simpleName
    }

    companion object {
        val duringPlayTriggers: Set<Trigger> = setOf(
            OnPlay,
            OnDraw,
            OnTurnStart,
            OnDeactivation,
            OnDeath,
            OnDiscard
        )
    }
}

/**
 * This is to be used in cases where there is no trigger or the trigger is not relevant.
 */
object Blank : Trigger()
object OnPlay : Trigger()
object OnDraw : Trigger()
object OnTurnStart : Trigger()
object OnDeactivation : Trigger()
object OnDeath : Trigger()
object OnDiscard : Trigger()
object OnSpecial : Trigger()
object OnCreation : Trigger()
object OnTick : Trigger()
