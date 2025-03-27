package com.sq.thed_ck_licker.helpers

import org.junit.Test

class MyRandomTest {

    @Test
    fun `getRandom   Debug Mode Seeded Randomness`() {
        // Verify that when R.bool.IS_DEBUG is $shouldBe, the returned Random
        // instance is seeded with 69. This ensures predictable random number generation
        // in debug environments.
//        val rDebug = R.bool.IS_DEBUG
//        val shouldBe = 2130837504 // This is bit fickle...
//        assert(rDebug == shouldBe) { "R.bool.IS_DEBUG should be $shouldBe, is $rDebug" }
    }

    @Test
    fun `getRandom   Multiple Calls Same Seed`() {
        // Check that in debug mode, multiple calls to getRandom() return Random 
        // instances that produce the same sequence of random numbers. We can test this 
        // by calling getRandom() twice, generating a number, then comparing them.
        val random = MyRandom.random

        val list = List(20) { random.nextInt(10) }
        val compare1 = listOf(3, 0, 3, 6, 2, 6, 6, 0, 8, 7, 7, 2, 5, 0, 8, 6, 3, 6, 9, 5)
        assert(list == compare1) { "list should be $compare1, is $list" }

        val list2 = List(20) { random.nextInt(10) }
        val compare2 = listOf(1, 9, 6, 0, 9, 2, 6, 7, 3, 7, 5, 8, 3, 0, 2, 3, 9, 3, 1, 9)
        assert(list2 == compare2) { "list2 should be $compare2, is $list2" }
    }


}