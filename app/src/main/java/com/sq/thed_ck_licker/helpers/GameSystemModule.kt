package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.RewardSystem
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
        rewardSystem: RewardSystem
    ): WorldCreationSystem {
        return WorldCreationSystem(
            componentManager,
            playerSystem,
            merchantSystem,
            rewardSystem
        )
    }

    @Provides
    @Singleton
    fun provideComponentManager(): ComponentManager {
        return ComponentManager()
    }
}