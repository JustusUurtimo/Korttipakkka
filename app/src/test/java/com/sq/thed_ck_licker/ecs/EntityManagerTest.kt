package com.sq.thed_ck_licker.ecs

import com.sq.thed_ck_licker.ecs.managers.EntityManager
import org.junit.Test

class EntityManagerTest {


    @Test
    fun `getPlayerID   returns correct ID`() {
        val playerId = EntityManager.getPlayerID()
        assert(playerId == -1) { "getPlayerID should return -1, but was $playerId" }
    }

    @Test
    fun `createNewEntity   multiple calls and single calls and all`() {

        // Player call
        val playerId = EntityManager.getPlayerID()
        assert(playerId == -1) { "getPlayerID should return -1, but was $playerId" }

        // First call
        val entityId = EntityManager.createNewEntity()
        assert(entityId == 1) { "First entity ID should be 1, was $entityId" }

        // 2 to 10 calls
        for (i in 2..10) {
            val entityId = EntityManager.createNewEntity()
            assert(entityId == i) { "Entity ID should be ${i}, was $entityId" }
        }

        // 11 to 100 calls
        for (i in 11..100) {
            val entityId = EntityManager.createNewEntity()
            assert(entityId == i) { "Entity ID should be ${i}, was $entityId" }
        }
    }
}