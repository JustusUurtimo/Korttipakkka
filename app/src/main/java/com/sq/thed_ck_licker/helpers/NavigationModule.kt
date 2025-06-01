package com.sq.thed_ck_licker.helpers

import android.content.Context
import com.sq.thed_ck_licker.MainActivity
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.NavControllerDispatcher
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.NavigationDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideNavigationDispatcher(
        @ApplicationContext context: Context
    ): NavigationDispatcher {
        val navController = (context as GameNavigation).navController
        return NavControllerDispatcher(navController)
    }
}