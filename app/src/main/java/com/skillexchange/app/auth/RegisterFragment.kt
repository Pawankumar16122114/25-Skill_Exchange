package com.skillexchange.app.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skillexchange.app.databinding.FragmentRegisterBinding
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.viewmodel.AuthState
import com.skillexchange.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardRegister.translationY = 80f
        binding.cardRegister.alpha = 0f
        binding.cardRegister.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(100).start()

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            var hasError = false
            if (name.isEmpty()) { binding.tilName.error = "Name required"; hasError = true }
            if (email.isEmpty()) { binding.tilEmail.error = "Email required"; hasError = true }
            if (password.length < 6) { binding.tilPassword.error = "Min 6 characters"; hasError = true }
            if (password != confirm) { binding.tilConfirmPassword.error = "Passwords don't match"; hasError = true }
            if (hasError) return@setOnClickListener

            clearErrors()
            viewModel.register(email, password, name)
        }

        binding.tvLoginLink.setOnClickListener {
            (activity as? AuthActivity)?.showLogin()
        }

        observeState()
    }

    private fun clearErrors() {
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.show()
                        binding.btnRegister.isEnabled = false
                    }
                    is AuthState.Success -> {
                        binding.progressBar.hide()
                        (activity as? AuthActivity)?.navigateToMain()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.hide()
                        binding.btnRegister.isEnabled = true
                        binding.tvError.text = state.message
                        binding.tvError.show()
                    }
                    is AuthState.Idle -> {
                        binding.progressBar.hide()
                        binding.btnRegister.isEnabled = true
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
