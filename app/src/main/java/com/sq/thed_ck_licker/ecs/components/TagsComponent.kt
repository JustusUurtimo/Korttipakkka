package com.sq.thed_ck_licker.ecs.components


data class TagsComponent(private val tags: List<CardTag> = emptyList()) : Component {

    enum class CardTag { CARD, MERCHANT, CORRUPTED }

    fun getTags(): List<CardTag> {
        return this.tags
    }

    fun cardIsMerchant(): Boolean {
        return this.tags.contains(CardTag.MERCHANT)
    }

    fun addTag(tag: CardTag) {
        this.tags.plus(tag)
    }
}