package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.CardCreationHelperSystems
import com.sq.thed_ck_licker.ecs.systems.helperSystems.MultiplierSystem
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
    fun provideCardsSystem(multiSystem: MultiplierSystem, playerSystem: PlayerSystem): CardsSystem {
        return CardsSystem(multiSystem, playerSystem)
    }

    @Provides
    @Singleton
    fun provideCardCreationSystem(
        cardCreationHelperSystems: CardCreationHelperSystems,
        cardBuilder: CardBuilderSystem,
        gameNavigator: GameNavigator
    ): CardCreationSystem {
        return CardCreationSystem(cardCreationHelperSystems, cardBuilder, gameNavigator)
    }

    @Provides
    @Singleton
    fun provideCardBuilderSystem(componentManager: ComponentManager): CardBuilderSystem {
        return CardBuilderSystem(componentManager)
    }

    @Provides
    @Singleton
    fun provideCardPullSystem(cardsSystem: CardsSystem, playerSystem: PlayerSystem): CardPullingSystem {
        return CardPullingSystem(cardsSystem, playerSystem)
    }

}