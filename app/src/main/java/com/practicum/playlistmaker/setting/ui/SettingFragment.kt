package com.practicum.playlistmaker.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.util.MyApplication
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private val viewModel: SettingViewModel by viewModel()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getThemeLiveData().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
            (requireContext().applicationContext as MyApplication).switchTheme(it)
        }
        with(binding) {
            themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.saveTheme(checked) }
            helpButton.setOnClickListener { viewModel.supportContact() }
            linkButton.setOnClickListener { viewModel.shareApp() }
            userAgreementButton.setOnClickListener { viewModel.openTerms() }
        }
    }
}