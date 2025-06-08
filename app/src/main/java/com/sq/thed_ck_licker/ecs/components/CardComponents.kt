package com.sq.thed_ck_licker.ecs.components

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.R


enum class CardTag { CARD, MERCHANT }


data class ImageComponent(@DrawableRes private val cardImage: Int = R.drawable.placeholder) {
    fun getImage(): Int {
        return this.cardImage
    }
}

data class IdentificationComponent(private val name: String = "Placeholder", private val characterId: Int?) {
    fun getName(): String {
        return this.name
    }
    fun getCharacterId(): Int? {
        return this.characterId
    }

}


data class TagsComponent(private val tags: List<CardTag> = emptyList()) {
    fun getTags(): List<CardTag> {
        return this.tags
    }
    fun cardIsMerchant(): Boolean {
        return this.tags.contains(CardTag.MERCHANT)
    }

}


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
) {
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

data class DurationComponent(
    private val duration: Int = -1,
    private val infinite: Boolean = false
) {
    fun getDuration(): Int {
        return this.duration
    }
    fun isInfinite(): Boolean {
        return this.infinite
    }
}