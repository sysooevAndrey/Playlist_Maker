package com.practicum.playlistmaker.setting.domain.impl

import com.practicum.playlistmaker.setting.domain.api.SharingInteractor
import com.practicum.playlistmaker.setting.domain.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun shareApp() = sharingRepository.shareApp()
    override fun supportContact() = sharingRepository.supportContact()
    override fun openTerms() = sharingRepository.openTerms()
}