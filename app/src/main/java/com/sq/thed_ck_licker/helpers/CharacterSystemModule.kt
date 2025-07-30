package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.dataStores.SettingsRepository
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
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
object CharacterSystemModule {

    @Provides
    @Singleton
    fun providePlayerSystem(
        cardCreationSystem: CardCreationSystem,
        settings: SettingsRepository
    ): PlayerSystem {
        return PlayerSystem(cardCreationSystem, settings)
    }

    @Provides
    @Singleton
    fun provideMerchantSystem(
        playerSystem: PlayerSystem,
        cardCreationSystem: CardCreationSystem,
        cardsSystem: CardsSystem
    ): MerchantSystem {
        return MerchantSystem(cardCreationSystem, playerSystem, cardsSystem)
    }
}