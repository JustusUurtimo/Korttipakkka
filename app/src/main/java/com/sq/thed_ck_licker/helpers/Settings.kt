package com.sq.thed_ck_licker.helpers

import androidx.compose.runtime.mutableStateOf

object Settings { //This should not be object, but that is more rework than i care for now
    val areRealTimeThingsEnabled = mutableStateOf(true)
    val addForestPackage = mutableStateOf(true)
    val addBaseTestPackage = mutableStateOf(false)
}