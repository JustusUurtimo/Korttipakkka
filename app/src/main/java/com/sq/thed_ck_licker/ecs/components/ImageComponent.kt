package com.sq.thed_ck_licker.ecs.components

import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R


data class ImageComponent(@DrawableRes private val cardImage: Int = R.drawable.placeholder) :
    Component {
    fun getImage(): Int {
        return this.cardImage
    }
}
