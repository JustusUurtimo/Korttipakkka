package com.sq.thed_ck_licker.ecs.components


data class TagsComponent(private val tags: List<Tag> = emptyList()) : Component {
    constructor(vararg tags: Tag) : this(tags.toList())

    enum class Tag { CARD, MERCHANT, CORRUPTED, FOREST, }

    fun getTags(): List<Tag> {
        return this.tags
    }

    fun cardIsMerchant(): Boolean {
        return this.tags.contains(Tag.MERCHANT)
    }

    fun addTag(tag: Tag) {
        this.tags.plus(tag)
    }
}