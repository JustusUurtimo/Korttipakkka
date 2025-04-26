package com.sq.thed_ck_licker.ecs.components

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class ScoreComponent(var mutableStateScore: MutableIntState) {
    constructor(score: Int = 0) : this(mutableIntStateOf(score))

    var score
        get() = this.mutableStateScore.intValue
        set(value) {
            println("Setting score to $value")
           this.mutableStateScore.intValue = value
        }
}

fun ScoreComponent.combineScoreComponents(other: ScoreComponent): ScoreComponent {
//    this.score += other.score
//    return this
    return ScoreComponent(this.score + other.score)
}