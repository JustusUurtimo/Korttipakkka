package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.WorldCreationSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameSystemModule {

    @Provides
    @Singleton
    fun provideWorldCreationSystem(
        componentManager: ComponentManager,
        playerSystem: PlayerSystem,
        merchantSystem: MerchantSystem,
        descriptionSystem: DescriptionSystem
    ): WorldCreationSystem {
        return WorldCreationSystem(
            componentManager,
            playerSystem,
            merchantSystem,
            descriptionSystem
        )
    }

    @Provides
    @Singleton
    fun provideComponentManager(): ComponentManager {
        return ComponentManager()
    }
}