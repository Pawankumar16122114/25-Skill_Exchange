package com.skillexchange.app.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skillexchange.app.databinding.FragmentLoginBinding
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.viewmodel.AuthState
import com.skillexchange.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate entrance
        binding.cardLogin.translationY = 80f
        binding.cardLogin.alpha = 0f
        binding.cardLogin.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(100).start()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) { binding.tilEmail.error = "Email required"; return@setOnClickListener }
            if (password.isEmpty()) { binding.tilPassword.error = "Password required"; return@setOnClickListener }

            binding.tilEmail.error = null
            binding.tilPassword.error = null
            viewModel.login(email, password)
        }

        binding.tvRegisterLink.setOnClickListener {
            (activity as? AuthActivity)?.showRegister()
        }

        // Clear errors on text change
        binding.etEmail.doOnTextChanged { _, _, _, _ -> binding.tilEmail.error = null }
        binding.etPassword.doOnTextChanged { _, _, _, _ -> binding.tilPassword.error = null }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.show()
                        binding.btnLogin.isEnabled = false
                    }
                    is AuthState.Success -> {
                        binding.progressBar.hide()
                        binding.btnLogin.isEnabled = true
                        (activity as? AuthActivity)?.navigateToMain()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.hide()
                        binding.btnLogin.isEnabled = true
                        binding.tvError.text = state.message
                        binding.tvError.show()
                    }
                    is AuthState.Idle -> {
                        binding.progressBar.hide()
                        binding.btnLogin.isEnabled = true
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
