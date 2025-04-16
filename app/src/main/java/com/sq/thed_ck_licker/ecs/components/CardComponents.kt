package com.sq.thed_ck_licker.ecs.components

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.sq.thed_ck_licker.R
import kotlinx.parcelize.Parcelize

// Enums
enum class CardClassification { EVIL, GOOD, MISC }
enum class CardEffectType { HP_REGEN, HEAL, DAMAGE, MAX_HP, DOUBLE_TROUBLE, REVERSE_DAMAGE, SHOP_COUPON }
enum class CardEffectValue(val value: Float) {
    DAMAGE_5(5f), DAMAGE_6(6f), HEAL_5(5f), HEAL_2(2f), HEAL_10(10f),
    MAX_HP_2(2f), DOUBLE_TROUBLE(0f), REVERSE_DAMAGE(0f), SHOP_COUPON(100f)
}

enum class CardTag { CARD }

@Parcelize
data class CardEffect(
    val classification: CardClassification,
    val effectType: CardEffectType,
    val effectValue: CardEffectValue
) : Parcelable

data class ImageComponent(@DrawableRes val cardImage: Int = R.drawable.placeholder)

data class DescriptionComponent(var description: MutableState<String>) {
    constructor(desc: String = "") : this(mutableStateOf(desc))
}

fun DescriptionComponent.addScore(scoreC: ScoreComponent) {
    description.value += "Get ${scoreC.score.intValue} points"
}

fun DescriptionComponent.addHealth(healthC: HealthComponent) {
    if (healthC.health.floatValue > 0) {
        description.value += "Heal for ${healthC.health.floatValue} points"
    } else if (healthC.health.floatValue < 0) {
        description.value += "Lose ${healthC.health.floatValue} health"
    }

    if (healthC.maxHealth.floatValue > 0) {
        description.value += "Gain ${healthC.maxHealth.floatValue} max health"
    } else if (healthC.maxHealth.floatValue < 0) {
        description.value += "Lose ${healthC.maxHealth.floatValue} max health"
    }
}

data class NameComponent(val name: String = "Placeholder")

data class TagsComponent(val tags: List<CardTag> = emptyList())

/**
 * Used to make more complex activations
 * The functions take the target entity id as a parameter.
 * Then you just make what you want in the function body.
 * And maaaagic
 */
data class EffectComponent(
    val onDeath: (Int) -> Unit = {},
    val onSpawn: (Int) -> Unit = {},
    val onTurnStart: (Int) -> Unit = {},
    val onPlay: (Int, Int) -> Unit = { _, _ -> },
    val onDeactivate: (Int, Int) -> Unit = {_, _ ->},
)

/**
 * Used to keep track of how many times a card has been activated
 *
 *
 * TODO: Think long and hard if there is way or need for this to be event, observer or subscribe style thing.
 *  I mean it would be nice and make sense that this gets hooked up into things.
 *  That could allow us to not farm this to every thing and risk duplicate counting.
 */
data class ActivationCounterComponent(
    var activations: MutableIntState,
    var deactivations: MutableIntState
) {
    constructor(
        activations: Int = 0,
        deactivations: Int = 0
    ) : this(mutableIntStateOf(activations), mutableIntStateOf(deactivations))
}

fun ActivationCounterComponent.activate() {
    this.activations.intValue += 1
    println("This has been activated ${this.activations.intValue} times")
}

fun ActivationCounterComponent.deactivate() {
    this.deactivations.intValue += 1
    println("This has been deactivated ${this.deactivations.intValue} times")
}

data class DurationComponent(val duration: Int = -1, val infinite: Boolean = false)