package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent

class DescriptionSystem {
    // In future we may want to have version to update just single entity too
    // But that is future problem
    // We will probably want method for updating range of entities too.
    fun updateAllDescriptions(componentManager: ComponentManager) {
        print("#######################################################")
        val entitiesWithDescription =
            componentManager.getEntitiesWithComponent(DescriptionComponent::class)
        print(entitiesWithDescription)
        print("#######################################################")
        /* TODO this needs to go over them and check what other components they have
        *   For example if they have ScoreComponent, description should say "Score: ${scoreComponent.score}"
        *   and so on.
        *   Similarly the TagsComponent should be updated based on what components they have.
        *   But here i fear the performance will be so horrible.
        *   I know I should not optimize before but i just fear for the system.
        *   Maybe it is best to make it and care about it when it is needed.
        *   As i think we will need this system anyways when we start to modify the effects on runtime
        */


        /* TODO this is really horrific code in many ways...
        *   But we shall improve in when needed
        *   This does so much repeating work
        *   This will be performance intense at some point
        *   Or maybe not cuz maps are crazyy
         */
        if (entitiesWithDescription != null) {
            for (entity in entitiesWithDescription) {
                print("####################################################")
                print(entity)
                print("####################################################")
                val comp = componentManager.getComponent(entity.key, ScoreComponent::class)
                val descComp =
                    componentManager.getComponent(entity.key, DescriptionComponent::class)
                descComp.description = "Get ${comp.score.intValue} points"
            }
        }

    }
}