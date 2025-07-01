package com.sq.thed_ck_licker.helpers

/**
 * This can be used when you want to display something to user but there is no ui for it yet.
 */
fun displayInfo(text: String) = println(text)

/**
 * I prefer to not use this one on empty list
 */
fun <T> List<T>.getRandomElement(): T {
    return this[MyRandom.getRandomInt(max = this.size)]
}


/**
 * I prefer to not use this one on empty list
 */
fun <T> List<T>.getRandomSubset(size: Int): List<T> {
    val copy = this.toMutableList()
    copy.shuffle(MyRandom.random)
    return copy.take(size)
}