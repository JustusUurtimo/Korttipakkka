package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DescriptionSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperSystemModule {

    @Provides
    @Singleton
    fun provideDescriptionSystem(componentManager: ComponentManager): DescriptionSystem {
        return DescriptionSystem(componentManager)
    }
    @Provides
    @Singleton
    fun provideMultiplierSystem(componentManager: ComponentManager): MultiplierSystem {
        return MultiplierSystem(componentManager)

    }
}