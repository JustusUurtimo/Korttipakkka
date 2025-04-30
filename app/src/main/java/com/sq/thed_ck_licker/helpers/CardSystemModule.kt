package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardBuilderSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardEffectSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
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
    fun provideCardsSystem(): CardsSystem {
        return CardsSystem()
    }

    @Provides
    @Singleton
    fun provideCardEffectSystem(): CardEffectSystem {
        return CardEffectSystem()
    }

    @Provides
    @Singleton
    fun provideCardCreationSystem(
        cardsSystem: CardsSystem,
        cardEffectSystem: CardEffectSystem,
        cardBuilder: CardBuilderSystem
    ): CardCreationSystem {
        return CardCreationSystem(cardsSystem, cardEffectSystem, cardBuilder)
    }

    @Provides
    @Singleton
    fun provideCardBuilderSystem(componentManager: ComponentManager): CardBuilderSystem {
        return CardBuilderSystem(componentManager)
    }

    @Provides
    @Singleton
    fun provideCardPullSystem(cardsSystem: CardsSystem): CardPullingSystem {
        return CardPullingSystem(cardsSystem)
    }

}