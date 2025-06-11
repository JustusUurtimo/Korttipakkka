package com.sq.thed_ck_licker.ecs.components

import androidx.annotation.DrawableRes
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