package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.PitSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PitSystemModule {

    @Provides
    @Singleton
    fun providePitSystem(
        playerSystem: PlayerSystem,
        merchantSystem: MerchantSystem,
        cardsSystem: CardsSystem
    ): PitSystem {
        return PitSystem(playerSystem, merchantSystem, cardsSystem)
    }
}