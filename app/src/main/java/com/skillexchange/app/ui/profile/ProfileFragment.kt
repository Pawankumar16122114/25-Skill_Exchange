package com.skillexchange.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skillexchange.app.R
import com.skillexchange.app.databinding.FragmentProfileBinding
import com.skillexchange.app.model.User
import com.skillexchange.app.util.SkillPointCalculator
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.util.showSnackbar
import com.skillexchange.app.util.toInitials
import com.skillexchange.app.viewmodel.ProfileUiState
import com.skillexchange.app.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val selectedSkills = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddSkill.setOnClickListener { showSkillPicker() }
        binding.btnSaveProfile.setOnClickListener { saveProfile() }
        binding.btnLogout.setOnClickListener { confirmLogout() }

        observeProfile()
        observeSaveState()
    }

    private fun observeProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ProfileUiState.Loading -> binding.progressBar.show()
                    is ProfileUiState.Success -> {
                        binding.progressBar.hide()
                        bindUser(state.user)
                    }
                    is ProfileUiState.Error -> {
                        binding.progressBar.hide()
                    }
                }
            }
        }
    }

    private fun bindUser(user: User) {
        binding.tvAvatarInitials.text = user.name.toInitials()
        binding.etName.setText(user.name)
        binding.etBio.setText(user.bio)
        binding.tvSkillPoints.text = "${user.skillPoints} pts"
        binding.tvCompletedSwaps.text = "${user.completedSwaps} swaps"
        binding.ratingBar.rating = user.trustScore
        binding.tvTrustLevel.text = SkillPointCalculator.getTrustLevel(user.trustScore)
        binding.tvTrustScore.text = String.format("%.1f", user.trustScore)

        // Animate stats
        binding.cardStats.animate().alpha(1f).translationY(0f).setDuration(400).start()

        // Populate skill chips
        selectedSkills.clear()
        selectedSkills.addAll(user.skills)
        refreshSkillChips()
    }

    private fun refreshSkillChips() {
        binding.chipGroupSkills.removeAllViews()
        selectedSkills.forEach { skill ->
            val chip = Chip(requireContext()).apply {
                text = skill
                isCloseIconVisible = true
                setChipBackgroundColorResource(R.color.chip_bg)
                setChipStrokeColorResource(R.color.chip_stroke)
                setTextColor(resources.getColor(R.color.chip_text, null))
                setOnCloseIconClickListener {
                    selectedSkills.remove(skill)
                    refreshSkillChips()
                }
            }
            binding.chipGroupSkills.addView(chip)
        }
    }

    private fun showSkillPicker() {
        val skills = resources.getStringArray(R.array.skills_list)
        val available = skills.filter { it !in selectedSkills }.toTypedArray()
        if (available.isEmpty()) {
            showSnackbar("You've added all skills!")
            return
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add a Skill")
            .setItems(available) { _, which ->
                selectedSkills.add(available[which])
                refreshSkillChips()
            }
            .show()
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val bio = binding.etBio.text.toString().trim()
        if (name.isEmpty()) { binding.tilName.error = "Name required"; return }
        binding.tilName.error = null
        viewModel.updateProfile(name, bio, selectedSkills.toList())
    }

    private fun observeSaveState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveState.collect { saved ->
                when (saved) {
                    true -> showSnackbar("Profile saved! ✅")
                    false -> showSnackbar("Failed to save profile")
                    null -> {}
                }
            }
        }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                viewModel.logout()
                requireActivity().finish()
                startActivity(
                    android.content.Intent(
                        requireContext(),
                        com.skillexchange.app.auth.AuthActivity::class.java
                    )
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
