package com.sq.thed_ck_licker.card

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R
import kotlinx.parcelize.Parcelize


enum class CardClassification {
    EVIl, GOOD, MISC
}

enum class CardEffectType {
    HP_REGEN, HEAL, DAMAGE, MAX_HP, DOUBLE_TROUBLE, REVERSE_DAMAGE, SHOP_COUPON
}

enum class CardEffectValue(val value: Float) {
    DAMAGE_5(5f),
    DAMAGE_6(6f),
    HEAL_5(-5f),
    HEAL_2(-2f),
    HEAL_10(-10f),
    MAX_HP_2(2f),
    DOUBLE_TROUBLE(0f),
    REVERSE_DAMAGE(0f),
    SHOP_COUPON(100f)
}

enum class Effect(
    val CardClassification: CardClassification,
    val CardEffectType: CardEffectType,
    val CardEffectValue: CardEffectValue
) {
    NO_CARDS(
        CardClassification.GOOD,
        CardEffectType.HEAL,
        CardEffectValue.DOUBLE_TROUBLE
    ),
    HP_REGEN_5(
        CardClassification.GOOD,
        CardEffectType.HP_REGEN,
        CardEffectValue.HEAL_5
    ),
    MAX_HP_2(
        CardClassification.GOOD,
        CardEffectType.MAX_HP,
        CardEffectValue.MAX_HP_2
    ),
    HEAL_5(
       CardClassification.GOOD,
        CardEffectType.HEAL,
        CardEffectValue.HEAL_5
    ),
    HEAL_2(
        CardClassification.GOOD,
        CardEffectType.HEAL,
        CardEffectValue.HEAL_2
    ),
    HEAL_10(
        CardClassification.GOOD,
        CardEffectType.HEAL,
        CardEffectValue.HEAL_10
    ),
    DAMAGE_5(
        CardClassification.EVIl,
        CardEffectType.DAMAGE,
        CardEffectValue.DAMAGE_5
    ),
    DAMAGE_6(
        CardClassification.EVIl,
        CardEffectType.DAMAGE,
        CardEffectValue.DAMAGE_6
    ),
    DOUBLE_TROUBLE(
        CardClassification.MISC,
        CardEffectType.DOUBLE_TROUBLE,
        CardEffectValue.DOUBLE_TROUBLE
    ),
    REVERSE_DAMAGE(
        CardClassification.MISC,
        CardEffectType.REVERSE_DAMAGE,
        CardEffectValue.REVERSE_DAMAGE
    ),
    SHOP_COUPON(
        CardClassification.MISC,
        CardEffectType.SHOP_COUPON,
        CardEffectValue.SHOP_COUPON
    )

}

@Parcelize
data class CardClass(
    val id: Int,
    @DrawableRes val cardImage: Int,
    val effect: Effect
) : Parcelable

object CardData {
    var cards = ArrayDeque(
        listOf(
            CardClass(
                id = 1,
                cardImage = R.drawable.damage_5,
                Effect.DAMAGE_5
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.damage_6,
                Effect.DAMAGE_6
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_5,
                Effect.HEAL_5
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_10,
                Effect.HEAL_10
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_2,
                Effect.HEAL_2
            ),
            CardClass(
                id = 3,
                cardImage = R.drawable.hp_regen,
                Effect.HP_REGEN_5
            ),
            CardClass(
                id = 4,
                cardImage = R.drawable.max_hp_2,
                Effect.MAX_HP_2
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.double_trouble,
                Effect.DOUBLE_TROUBLE
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.reverse_damage,
                Effect.REVERSE_DAMAGE
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.shop_coupon,
                Effect.SHOP_COUPON
            )
        )
    )
}