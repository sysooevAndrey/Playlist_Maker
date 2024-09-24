package com.practicum.playlistmaker.setting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.setting.domain.api.SharingInteractor
import com.practicum.playlistmaker.setting.domain.api.ThemeInteractor


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
}