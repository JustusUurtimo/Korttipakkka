package com.sq.thed_ck_licker.ecs.managers

import android.util.Log

/**
 * Fixed Id things can grow towards negative and normal dynamic ones can be normal integers
 */
object EntityManager {

    private const val PLAYER_ID: EntityId = -1
    fun getPlayerID() = PLAYER_ID

    private const val REGULAR_MERCHANT_ID: EntityId = -2
    fun getRegularMerchantID() = REGULAR_MERCHANT_ID

    private const val REGULAR_REWARD_ID: EntityId = -10
    fun getRegularRewardID() = REGULAR_REWARD_ID

    private const val SPECIAL_REWARD_ID: EntityId = -11
    fun getSpecialRewardID() = SPECIAL_REWARD_ID

    private var nextID: EntityId = 1


    fun createNewEntity(): EntityId {
        val nextID2 = nextID
        nextID++
        Log.i("EntityManager", "New entity created with ID: $nextID2")
        return nextID2
    }

}

fun generateEntity() = EntityManager.createNewEntity()

typealias EntityId = Int

