package com.sq.thed_ck_licker.ecs

import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import kotlin.reflect.KClass

// Component Manager
class ComponentManager {
    private val components = mutableMapOf<KClass<*>, MutableMap<Int, Any>>()

    fun <T : Any> addComponent(entity: Int, component: T) {
        components.getOrPut(component::class) { mutableMapOf() }[entity] = component
    }

    fun <T : Any> getComponent(entity: Int, componentClass: KClass<T>): T {
        val componentMap = components[componentClass]
            ?: throw IllegalStateException("No components of type ${componentClass.simpleName} found")

        val component = componentMap[entity]
            ?: throw IllegalStateException("Component of type ${componentClass.simpleName} not found for entity $entity")

        // Check the type of the component before casting
        if (componentClass.isInstance(component)) {
            @Suppress("UNCHECKED_CAST") // Safe cast after type check
            return component as T
        } else {
            throw IllegalStateException("Component for entity $entity is not of type ${componentClass.simpleName}")
        }
    }

//    fun <T : Any> getEntitiesWithComponent(component: T): Map<Int, Any>? {
//        val eka = components[component::class]
//        println("eka $eka")
//        return eka
//    }

    fun <T : Any> getEntitiesWithComponent(componentClass: KClass<T>): Map<Int, Any>? {
        return components[componentClass]
    }

    fun getEntitiesWithTags(tags: List<CardTag>, strictAll: Boolean = false): Map<Int, Any>? {
//        println("all all $components")
//        println("thing thing ${components[TagsComponent::class]}")
        val entities = getEntitiesWithComponent(TagsComponent::class)
        println("All the entities: $entities")
        val matchingEntities = entities?.filter { (entity, value) ->
//            if (strictAll) {
            (value as TagsComponent).tags.containsAll(tags)
//            } else {
//                (value as TagsComponent).tags.any(tags)
//                (value as TagsComponent).tags
//            }
        }
        println("All the matchingEntities: $matchingEntities")

        return matchingEntities
    }

    fun getAllComponentsOfEntity(entityId: Int): List<Any> {
        val result = ArrayList<Any>()
        for (componentMap in components.values) {
            val component = componentMap[entityId]
//            println("component $component")
            if (component != null) {
//                result.plus(component)
//                println("HAi hai hai")
                result.add(component)
            }
        }
//        println("result $result")
        return result
    }
}