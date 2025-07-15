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

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object SettingsKeys {
        val REAL_TIME_PLAYER_DAMAGE_ENABLED = booleanPreferencesKey("real_time_player_damage_enabled")
        val ADD_BASE_TEST_PACKAGE = booleanPreferencesKey("add_base_test_package")
        val ADD_FOREST_PACKAGE = booleanPreferencesKey("add_forest_package")
    }

    suspend fun setRealTimePlayerDamageEnabled(enabled: Boolean) {
        context.dataStore.edit { it[REAL_TIME_PLAYER_DAMAGE_ENABLED] = enabled }
    }

    suspend fun setAddBaseTestPackage(enabled: Boolean) {
        context.dataStore.edit { it[ADD_BASE_TEST_PACKAGE] = enabled }
    }

    suspend fun setAddForestPackage(enabled: Boolean) {
        context.dataStore.edit { it[ADD_FOREST_PACKAGE] = enabled }
    }

    val isRealTimePlayerDamageEnabled: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[REAL_TIME_PLAYER_DAMAGE_ENABLED] ?: false
        }

    val isBaseTestPackageAdded: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[ADD_BASE_TEST_PACKAGE] ?: false
        }

    val isForestPackageAdded: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[ADD_FOREST_PACKAGE] ?: false
        }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

}