package com.sq.thed_ck_licker.helpers

import kotlin.random.Random

class FixedRandom(val randomSequence: List<Int>) : Random() {
    override fun nextBits(bitCount: Int): Int {
        TODO("Not yet implemented")
    }

    var current = 0
    override fun nextInt(min: Int, max: Int): Int {
        val result = randomSequence[current]
        current += 1 % randomSequence.size
        return result
    }
}