package com.practicum.playlistmaker.setting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.setting.domain.api.SharingInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private var themeLiveData = MutableLiveData<Boolean>()

    init {
        themeInteractor.getTheme(object : ThemeInteractor.ThemeConsumer {
            override fun consume(isDarkTheme: Boolean) {
                themeLiveData.value = isDarkTheme
            }
        })
    }

    fun saveTheme(checked: Boolean) {
        themeInteractor.saveTheme(checked)
        themeLiveData.value = checked
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun supportContact() = sharingInteractor.supportContact()
    fun openTerms() = sharingInteractor.openTerms()
    fun getThemeLiveData(): LiveData<Boolean> = themeLiveData

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[APPLICATION_KEY] as App)
                val themeInteractor =
                    Creator.provideThemeInteractor(context)
                val shareInteractor =
                    Creator.provideShareInteractor(context)
                SettingViewModel(themeInteractor, shareInteractor)
            }
        }
    }
}