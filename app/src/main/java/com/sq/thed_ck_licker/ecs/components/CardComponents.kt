package com.sq.thed_ck_licker.ecs.components

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
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

enum class CardTag {CARD}

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
    if (healthC.health.floatValue < 0) {
        description.value += "Heal for ${healthC.health.floatValue * -1} points"
    } else if (healthC.health.floatValue > 0) {
        description.value += "Lose ${healthC.health.floatValue} health"
    }

    if (healthC.maxHealth.floatValue < 0) {
        description.value += "Gain ${healthC.maxHealth.floatValue * -1} max health"
    } else if (healthC.maxHealth.floatValue > 0) {
        description.value += "Lose ${healthC.health.floatValue} max health"
    }
}

data class NameComponent(val name: String = "Placeholder")

@Deprecated("I think will decide against this. \n And each tag should have its own component")
data class TagsComponent(val tags: List<CardTag> = emptyList())

