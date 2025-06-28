package com.sq.thed_ck_licker.ecs.components.misc

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class ScoreComponent(private var score: MutableIntState, val currentRewardTier: MutableIntState) {
    constructor(score: Int = 0, currentRewardTier: Int = 0) : this(mutableIntStateOf(score), mutableIntStateOf(currentRewardTier))

    fun addScore(score: Int): Int {
        this.score.intValue += score
        return this.score.intValue
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

    fun combineScoreComponents(other: ScoreComponent): ScoreComponent {
        return ScoreComponent(this.getScore() + other.getScore())
    }

    fun getRewardTier(): Int {
        return currentRewardTier.intValue
    }

    fun setRewardTier(tier: Int) {
        this.currentRewardTier.intValue = tier
    }




}