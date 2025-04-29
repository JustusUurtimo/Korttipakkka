package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathViewSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewSystemModule {

    @Provides
    @Singleton
    fun provideDeathViewSystem(): DeathViewSystem {
        return DeathViewSystem()
    }

}