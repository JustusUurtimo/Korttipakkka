package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DescriptionSystem
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
}