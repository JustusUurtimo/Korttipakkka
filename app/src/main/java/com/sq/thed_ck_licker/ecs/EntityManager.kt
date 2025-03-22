package com.sq.thed_ck_licker.ecs


object EntityManager {

    private const val PLAYER_ID = 1
    fun getPlayerID() = PLAYER_ID

    //Remember to increment this by one every time you create a new entity with fixed id
    // It is bit junk but it works, good enough for now.
    private var nextID = 2

//    init {
//        nextID = 2
//    }


    fun createNewEntity(): Int {
        nextID++
        return nextID - 1
    }

}

fun generateEntity() = EntityManager.createNewEntity()