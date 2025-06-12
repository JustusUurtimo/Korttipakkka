package com.sq.thed_ck_licker.ecs.components

import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R


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
