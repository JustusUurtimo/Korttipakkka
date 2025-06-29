package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardSystemModule {

    @Provides
    @Singleton
    fun provideCardsSystem(playerSystem: PlayerSystem): CardsSystem {
        return CardsSystem(playerSystem)
    }

    @Provides
    @Singleton
    fun provideCardCreationSystem(
        gameNavigator: GameNavigator
    ): CardCreationSystem {
        return CardCreationSystem(gameNavigator)
    }

    @Provides
    @Singleton
    fun provideCardPullSystem(cardsSystem: CardsSystem, playerSystem: PlayerSystem): CardPullingSystem {
        return CardPullingSystem(cardsSystem, playerSystem)
    }

}