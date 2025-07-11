package com.sq.thed_ck_licker.dataStores

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "player_settings")

object SettingsKeys {
    val REAL_TIME_PLAYER_DAMAGE_ENABLED = booleanPreferencesKey("real_time_player_damage_enabled")
}

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val realTimePlayerDamageEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { it[SettingsKeys.REAL_TIME_PLAYER_DAMAGE_ENABLED] ?: true }

    suspend fun setRealTimePlayerDamageEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SettingsKeys.REAL_TIME_PLAYER_DAMAGE_ENABLED] = enabled }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

}