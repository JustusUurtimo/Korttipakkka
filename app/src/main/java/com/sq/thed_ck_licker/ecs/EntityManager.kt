package com.sq.thed_ck_licker.ecs

/**
 * Fixed Id things can grow towards negative and normal dynamic ones can be normal integers
 */
object EntityManager {

    private const val PLAYER_ID: EntityId = -1
    fun getPlayerID() = PLAYER_ID

    private const val REGULAR_MERCHANT_ID: EntityId = -2
    fun getRegularMerchantID() = REGULAR_MERCHANT_ID

    private var nextID: EntityId = 1


    fun createNewEntity(): EntityId {
        nextID++
        println("New entity created with ID: $nextID")
        return nextID - 1
    }
}

fun generateEntity() = EntityManager.createNewEntity()

typealias EntityId = Int
