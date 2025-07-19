package com.sq.thed_ck_licker.ecs.components.effectthing

interface Trigger

interface DuringPlay {
    companion object {
        val triggers: Set<Trigger> = setOf(
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
object Blank : Trigger
object OnPlay : Trigger, DuringPlay
object OnDraw : Trigger, DuringPlay
object OnTurnStart : Trigger, DuringPlay
object OnDeactivation : Trigger, DuringPlay
object OnDeath : Trigger, DuringPlay
object OnDiscard : Trigger, DuringPlay
object OnSpecial : Trigger
object OnCreation : Trigger
object OnTick : Trigger
