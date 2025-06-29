package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.Component

data class ScoreComponent(private var score: MutableIntState): Component {
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
    fun getScoreF(): Float {
        return this.score.intValue.toFloat()
    }

    fun setScore(score: Int) {
        this.score.intValue = score
    }
    fun combineScoreComponents(other: ScoreComponent): ScoreComponent {
        return ScoreComponent(this.getScore() + other.getScore())
    }

}
