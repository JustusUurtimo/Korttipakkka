package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

/**
 * Used to keep track of how many times a card has been activated
 *
 *
 *  Think long and hard if there is way or need for this to be event, observer or subscribe style thing.
 *  I mean it would be nice and make sense that this gets hooked up into things.
 *  That could allow us to not farm this to every thing and risk duplicate counting.
 */
data class ActivationCounterComponent(
    private var activations: MutableIntState,
    private var deactivations: MutableIntState
): Component {
    constructor(
        activations: Int = 0,
        deactivations: Int = 0
    ) : this(mutableIntStateOf(activations), mutableIntStateOf(deactivations))

    fun activate() {
        this.activations.intValue += 1
        Log.i(
            "ActivationCounterComponent",
            "This has been activated ${this.activations.intValue} times"
        )
    }

    fun deactivate() {
        this.deactivations.intValue += 1
        Log.i(
            "ActivationCounterComponent",
            "This has been deactivated ${this.deactivations.intValue} times"
        )
    }

    fun getActivations(): Int {
        return this.activations.intValue
    }

    fun getDeactivations(): Int {
        return this.deactivations.intValue
    }
}