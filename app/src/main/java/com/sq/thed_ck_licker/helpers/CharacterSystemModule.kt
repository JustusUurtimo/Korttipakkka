package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharacterSystemModule {

    @Provides
    @Singleton
    fun providePlayerSystem(): PlayerSystem {
        return PlayerSystem().apply {
            initPlayer()
        }
    }

    @Provides
    @Singleton
    fun provideMerchantSystem(): MerchantSystem {
        return MerchantSystem().apply {
            initRegularMerchant()
        }
    }
}