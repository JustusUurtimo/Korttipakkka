package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class ScoreComponent(var mutableStateScore: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))

    var score
        get() = mutableStateScore.intValue
        set(value) {
            mutableStateScore.intValue = value
        }
}
