package com.sq.thed_ck_licker.ecs.components

import javax.inject.Inject

data class DiscardDeckComponent @Inject constructor(private val discardDeck: MutableList<Int> = mutableListOf<Int>()) :
    Component {
    fun getSize(): Int {
        return this.discardDeck.size
    }

    fun getDiscardDeck(): MutableList<Int> {
        return this.discardDeck
    }

    fun removeCards(cards: List<Int>) {
        this.discardDeck.removeAll(cards)
    }

    fun addCard(cardId: Int) {
        this.discardDeck.add(cardId)
    }
}