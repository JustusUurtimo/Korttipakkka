package com.sq.thed_ck_licker.ecs.components

data class IdentificationComponent(private val name: String = "Placeholder", private val characterId: Int?):
    Component {
    fun getName(): String {
        return this.name
    }
    fun getCharacterId(): Int? {
        return this.characterId
    }
}
