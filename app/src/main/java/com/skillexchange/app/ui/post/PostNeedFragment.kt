package com.skillexchange.app.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skillexchange.app.R
import com.skillexchange.app.databinding.FragmentPostNeedBinding
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.util.showSnackbar
import com.skillexchange.app.viewmodel.PostState
import com.skillexchange.app.viewmodel.SwapViewModel
import kotlinx.coroutines.launch

class PostNeedFragment : Fragment() {

    private var _binding: FragmentPostNeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SwapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostNeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate form entrance
        binding.cardForm.translationY = 60f
        binding.cardForm.alpha = 0f
        binding.cardForm.animate().translationY(0f).alpha(1f).setDuration(400).start()

        // Setup skill spinner
        val skills = resources.getStringArray(R.array.skills_list)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            skills
        )
        binding.actvSkillRequired.setAdapter(adapter)

        binding.btnSubmitPost.setOnClickListener {
            submitPost()
        }

        observeState()
    }

    private fun submitPost() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val skill = binding.actvSkillRequired.text.toString().trim()

        var hasError = false
        if (title.isEmpty()) { binding.tilTitle.error = "Title required"; hasError = true }
        if (description.isEmpty()) { binding.tilDescription.error = "Description required"; hasError = true }
        if (skill.isEmpty()) { binding.tilSkillRequired.error = "Select a skill"; hasError = true }
        if (hasError) return

        binding.tilTitle.error = null
        binding.tilDescription.error = null
        binding.tilSkillRequired.error = null

        viewModel.createNeedPost(title, description, skill)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postState.collect { state ->
                when (state) {
                    is PostState.Loading -> {
                        binding.progressBar.show()
                        binding.btnSubmitPost.isEnabled = false
                    }
                    is PostState.Success -> {
                        binding.progressBar.hide()
                        binding.btnSubmitPost.isEnabled = true
                        binding.lottieSuccess.show()
                        binding.lottieSuccess.playAnimation()
                        showSnackbar(state.message)
                        clearForm()
                        viewModel.resetPostState()
                        // Hide lottie after 2s
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            binding.lottieSuccess.hide()
                        }, 2000)
                    }
                    is PostState.Error -> {
                        binding.progressBar.hide()
                        binding.btnSubmitPost.isEnabled = true
                        showSnackbar(state.message)
                        viewModel.resetPostState()
                    }
                    is PostState.Idle -> {
                        binding.progressBar.hide()
                        binding.btnSubmitPost.isEnabled = true
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.etTitle.setText("")
        binding.etDescription.setText("")
        binding.actvSkillRequired.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
