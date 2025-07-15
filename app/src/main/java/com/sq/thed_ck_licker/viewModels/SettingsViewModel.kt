package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
    private val settings: SettingsRepository
) : ViewModel() {

    private val _realTimePlayerDamageEnabled = MutableStateFlow(true)
    val realTimePlayerDamageEnabled: StateFlow<Boolean> = _realTimePlayerDamageEnabled.asStateFlow()

    private val _baseTestPackageAdded = MutableStateFlow(true)
    val baseTestPackageAdded: StateFlow<Boolean> = _baseTestPackageAdded.asStateFlow()

    private val _forestPackageAdded = MutableStateFlow(true)
    val forestPackageAdded: StateFlow<Boolean> = _forestPackageAdded.asStateFlow()

    init {
        viewModelScope.launch {
            settings.isRealTimePlayerDamageEnabled.collect { _realTimePlayerDamageEnabled.value = it }
            settings.isBaseTestPackageAdded.collect { _baseTestPackageAdded.value = it }
            settings.isForestPackageAdded.collect { _forestPackageAdded.value = it }
        }
    }

    fun toggleRealTimePlayerDamageEnabled() {
        viewModelScope.launch {
            settings.setRealTimePlayerDamageEnabled(!_realTimePlayerDamageEnabled.value)
        }
    }
    fun toggleBaseTestPackageAdded() {
        viewModelScope.launch {
            settings.setAddBaseTestPackage(!_baseTestPackageAdded.value)
        }
    }
    fun toggleForestPackageAdded() {
        viewModelScope.launch {
            settings.setAddForestPackage(!_forestPackageAdded.value)
        }
    }

    fun clear() {
        viewModelScope.launch {
            settings.clear()
        }
    }
}