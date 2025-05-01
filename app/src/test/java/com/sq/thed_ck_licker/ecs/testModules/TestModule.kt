package com.sq.thed_ck_licker.ecs.testModules

import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.helpers.CardSystemModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CardSystemModule::class]
)
object TestModule {
    @Provides
    fun provideMockCardPullingSystem(): CardPullingSystem {
        return mock(CardPullingSystem::class.java)
    }

    @Provides
    fun provideMockCardsSystem(): CardsSystem {
        return mock(CardsSystem::class.java)
    }

    @Provides
    fun provideMockCardCreationSystem(): CardCreationSystem {
        return mock(CardCreationSystem::class.java)
    }
}