package com.sq.thed_ck_licker.ecs.components

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R
import kotlinx.parcelize.Parcelize

// Enums
enum class CardClassification { EVIL, GOOD, MISC }
enum class CardEffectType { HP_REGEN, HEAL, DAMAGE, MAX_HP, DOUBLE_TROUBLE, REVERSE_DAMAGE, SHOP_COUPON }
enum class CardEffectValue(val value: Float) {
    DAMAGE_5(5f), DAMAGE_6(6f), HEAL_5(5f), HEAL_2(2f), HEAL_10(10f),
    MAX_HP_2(2f), DOUBLE_TROUBLE(0f), REVERSE_DAMAGE(0f), SHOP_COUPON(100f)
}

@Deprecated("I think will decide against this. \n And each tag should have its own component")
enum class CardTag { Card }

@Deprecated("This should not be used, instead use ImageComponent")
@Parcelize
data class CardIdentity(
    val id: Int, @DrawableRes val cardImage: Int
) : Parcelable

@Parcelize
data class CardEffect(
    val classification: CardClassification,
    val effectType: CardEffectType,
    val effectValue: CardEffectValue
) : Parcelable

data class ImageComponent(@DrawableRes val cardImage: Int = R.drawable.placeholder)

data class DescriptionComponent(var description: String = "This is simple placeholder description")

data class NameComponent(val name: String = "Placeholder")

@Deprecated("I think will decide against this. \n And each tag should have its own component")
data class TagsComponent(val tags: List<CardTag> = emptyList())

