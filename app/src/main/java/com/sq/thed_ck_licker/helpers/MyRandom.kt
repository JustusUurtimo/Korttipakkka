package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.helpers.MyRandom.random
import kotlin.random.Random

object MyRandom {

    val random = Random(69)

    fun getRandomInt(min: Int = 0, max: Int = 10) = random.nextInt(min, max)

}

/**
 * I prefer to not use this one on empty list
 */
fun <T> List<T>.getRandomElement(): T {
    return this[random.nextInt(this.size)]
}


