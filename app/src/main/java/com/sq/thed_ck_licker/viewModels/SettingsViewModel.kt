package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.dataStores.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _realTimePlayerDamageEnabled = MutableStateFlow(true)
    val realTimePlayerDamageEnabled: StateFlow<Boolean> = _realTimePlayerDamageEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            repository.realTimePlayerDamageEnabledFlow.collect { _realTimePlayerDamageEnabled.value = it }
        }
    }

    fun toggleRealTimePlayerDamageEnabled() {
        viewModelScope.launch {
            repository.setRealTimePlayerDamageEnabled(!_realTimePlayerDamageEnabled.value)
        }
    }

    fun clear() {
        viewModelScope.launch {
            repository.clear()
        }
    }
}