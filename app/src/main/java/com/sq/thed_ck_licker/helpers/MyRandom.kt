package com.sq.thed_ck_licker.helpers

import kotlin.random.Random

object MyRandom {

    var random = Random(69)

    fun getRandomInt(min: Int = 0, max: Int = 10) = random.nextInt(min, max)

}

/**
 * I prefer to not use this one on empty list
 */
fun <T> List<T>.getRandomElement(): T {
    return this[MyRandom.getRandomInt(max = this.size)]
}


