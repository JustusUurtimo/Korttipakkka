import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class MerchantComponent(
    private var merchantId: MutableIntState,
    private val activeMerchantSummonCard: MutableIntState
) {
    constructor(merchantId: Int = -1, activeMerchantSummonCard: Int = -1) : this(
        mutableIntStateOf(
            merchantId
        ), mutableIntStateOf(activeMerchantSummonCard)
    )

    fun getMerchantId(): Int {
        return this.merchantId.intValue
    }

    fun getActiveMerchantSummonCard(): Int {
        return this.activeMerchantSummonCard.intValue
    }

    fun setMerchantId(merchantId: Int) {
        this.merchantId.intValue = merchantId
    }

    fun setActiveMerchantSummonCard(activeMerchantSummonCard: Int) {
        this.activeMerchantSummonCard.intValue = activeMerchantSummonCard
    }
}
//this should be implemented after we refactor the card creations system
data class CardPriceComponent(private var price: MutableIntState) {
    constructor(price: Int = 50) : this(mutableIntStateOf(price))
}

data class EntityMemoryComponent(private var affinity: MutableIntState) {
    constructor(affection: Int = 0) : this(mutableIntStateOf(affection))

    fun getAffinity(): Int {
        return this.affinity.intValue
    }
    fun setAffinity(affection: Int) {
        this.affinity.intValue = affection
    }
    fun updateAffinity(amount: Int) {
        this.affinity.intValue += amount
    }
}





