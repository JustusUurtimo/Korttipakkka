package com.sq.thed_ck_licker.ecs.components

import javax.inject.Inject

data class DrawDeckComponent @Inject constructor(
    private val drawCardDeck: MutableList<Int>,
): Component {
    fun getSize(): Int {
        return this.drawCardDeck.size
    }

    fun getDrawCardDeck(): MutableList<Int> {
        return this.drawCardDeck
    }

    fun removeCards(cards: List<Int>) {
        this.drawCardDeck.removeAll(cards)
    }

    fun removeCard(cardId: Int) {
        this.drawCardDeck.remove(cardId)
    }
}
