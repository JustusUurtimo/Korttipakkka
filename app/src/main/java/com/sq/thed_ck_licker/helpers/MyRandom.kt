package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.helpers.MyRandom.random
import kotlin.random.Random

object MyRandom {
//     val isDebug = R.bool.IS_DEBUG
    // TODO make sure that IS_DEBUG is actually the thing it claims to be :raise_eyebrow:
    val random = if (R.bool.IS_DEBUG == 2130837504) Random(69) else Random(System.currentTimeMillis())

}

/**
 * I prefer to not use this one on empty list
 */
fun <T> List<T>.getRandomElement(): T {
    return this[random.nextInt(this.size)]
}

//fun <T> List<T>.getRandomElement(): T? {
//    return if (this.isNotEmpty()) this[random.nextInt(this.size)] else null
//}