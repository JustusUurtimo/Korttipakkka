package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class ScoreComponent(private var score: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))

    fun addScore(score: Int) {
        this.score.intValue += score
    }

    fun reduceScore(score: Int) {
        this.score.intValue -= score
    }

    fun getScore(): Int {
        return this.score.intValue
    }

    fun setScore(score: Int) {
        this.score.intValue = score
    }

}