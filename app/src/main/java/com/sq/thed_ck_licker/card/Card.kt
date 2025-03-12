package com.sq.thed_ck_licker.card

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R
import kotlinx.parcelize.Parcelize

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

enum class CardEffect(val type: CardEffectType, val value: CardEffectValue) {
    HP_REGEN_5(CardEffectType.HP_REGEN, CardEffectValue.HEAL_5),
    HEAL_5(CardEffectType.HEAL, CardEffectValue.HEAL_5),
    HEAL_2(CardEffectType.HEAL, CardEffectValue.HEAL_2),
    HEAL_10(CardEffectType.HEAL, CardEffectValue.HEAL_10),
    DAMAGE_5(CardEffectType.DAMAGE, CardEffectValue.DAMAGE_5),
    DAMAGE_6(CardEffectType.DAMAGE, CardEffectValue.DAMAGE_6),
    MAX_HP_2(CardEffectType.MAX_HP, CardEffectValue.MAX_HP_2),
    DOUBLE_TROUBLE(CardEffectType.DOUBLE_TROUBLE, CardEffectValue.DOUBLE_TROUBLE),
    REVERSE_DAMAGE(CardEffectType.REVERSE_DAMAGE, CardEffectValue.REVERSE_DAMAGE),
    SHOP_COUPON(CardEffectType.SHOP_COUPON, CardEffectValue.SHOP_COUPON)

}

@Parcelize
data class CardClass(
    val id: Int,
    @DrawableRes val cardImage: Int,
    val cardEffect: CardEffect
) : Parcelable

object CardData {
    var cards = ArrayDeque(
        listOf(
            CardClass(
                id = 1,
                cardImage = R.drawable.damage_5,
                CardEffect.DAMAGE_5
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.damage_6,
                CardEffect.DAMAGE_6
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_5,
                CardEffect.HEAL_5
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_10,
                CardEffect.HEAL_10
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.heal_2,
                CardEffect.HEAL_2
            ),
            CardClass(
                id = 3,
                cardImage = R.drawable.hp_regen,
                CardEffect.HP_REGEN_5
            ),
            CardClass(
                id = 4,
                cardImage = R.drawable.max_hp_2,
                CardEffect.MAX_HP_2
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.double_trouble,
                CardEffect.DOUBLE_TROUBLE
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.reverse_damage,
                CardEffect.REVERSE_DAMAGE
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.shop_coupon,
                CardEffect.SHOP_COUPON
            )
        )
    )
}