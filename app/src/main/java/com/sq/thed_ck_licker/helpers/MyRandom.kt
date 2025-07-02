package com.sq.thed_ck_licker.helpers

import kotlin.random.Random

object MyRandom {

    var random = Random(System.currentTimeMillis())

    fun getRandomInt(min: Int = 0, max: Int = 10) = random.nextInt(min, max)

}
