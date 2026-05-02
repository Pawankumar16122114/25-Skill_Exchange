package com.skillexchange.app.ui.offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skillexchange.app.R
import com.skillexchange.app.databinding.BottomSheetOfferBinding
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.viewmodel.PostState
import com.skillexchange.app.viewmodel.SwapViewModel
import kotlinx.coroutines.launch

class OfferBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetOfferBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SwapViewModel by viewModels()
    private lateinit var post: NeedPost

    companion object {
        private const val ARG_POST_ID = "post_id"
        private const val ARG_POST_TITLE = "post_title"
        private const val ARG_POST_AUTHOR_ID = "author_id"
        private const val ARG_SKILL_REQUIRED = "skill_required"

        fun newInstance(post: NeedPost) = OfferBottomSheet().apply {
            this.post = post
            arguments = Bundle().apply {
                putString(ARG_POST_ID, post.id)
                putString(ARG_POST_TITLE, post.title)
                putString(ARG_POST_AUTHOR_ID, post.authorId)
                putString(ARG_SKILL_REQUIRED, post.skillRequired)
            }
        }
    }

    override fun getTheme() = R.style.Theme_SkillExchange_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPostTitle.text = "Offering for: ${arguments?.getString(ARG_POST_TITLE)}"

        // Setup skill dropdown
        val skills = resources.getStringArray(R.array.skills_list)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
        binding.actvSkillOffered.setAdapter(adapter)

        // Hours slider
        binding.sliderHours.addOnChangeListener { _, value, _ ->
            binding.tvHoursValue.text = "${value.toInt()} hr = ${value.toInt()} pts"
        }
        binding.tvHoursValue.text = "1 hr = 1 pts"

        binding.btnSendOffer.setOnClickListener {
            val skillOffered = binding.actvSkillOffered.text.toString().trim()
            val hours = binding.sliderHours.value.toInt()
            val message = binding.etMessage.text.toString().trim()

            if (skillOffered.isEmpty()) {
                binding.tilSkillOffered.error = "Select your skill"
                return@setOnClickListener
            }
            binding.tilSkillOffered.error = null
            viewModel.makeSwapOffer(post, skillOffered, hours, message)
        }

        observeOfferState()
    }

    private fun observeOfferState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.offerState.collect { state ->
                when (state) {
                    is PostState.Loading -> {
                        binding.progressBar.show()
                        binding.btnSendOffer.isEnabled = false
                    }
                    is PostState.Success -> {
                        binding.progressBar.hide()
                        binding.lottieSuccess.show()
                        binding.lottieSuccess.playAnimation()
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            dismiss()
                        }, 1500)
                    }
                    is PostState.Error -> {
                        binding.progressBar.hide()
                        binding.btnSendOffer.isEnabled = true
                        binding.tvError.text = state.message
                        binding.tvError.show()
                        viewModel.resetOfferState()
                    }
                    is PostState.Idle -> {
                        binding.progressBar.hide()
                        binding.btnSendOffer.isEnabled = true
                        binding.tvError.hide()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
