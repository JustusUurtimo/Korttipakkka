package com.sq.thed_ck_licker.helpers

import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeckSystemModule {

    @Provides
    @Singleton
    fun provideDrawDeckComponent(): DrawDeckComponent {
        return DrawDeckComponent(mutableListOf())
    }

    @Provides
    @Singleton
    fun provideDiscardDeckComponent(): DiscardDeckComponent {
        return DiscardDeckComponent(mutableListOf())
    }
}