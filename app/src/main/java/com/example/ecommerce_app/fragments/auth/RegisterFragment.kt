package com.example.ecommerce_app.fragments.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.data.User
import com.example.ecommerce_app.databinding.FragmentRegisterBinding
import com.example.ecommerce_app.utils.RegisterValidation
import com.example.ecommerce_app.utils.Resource
import com.example.ecommerce_app.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchProgressCoroutine()
        onRegisterClick()
        onValidationHandle()

        binding.tvRegLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onValidationHandle() {
        lifecycleScope.launch {
            viewModel.validation.collect{ validation->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etRegEmail.apply{
                            requestFocus()
                            error = validation.email.msg
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etRegPassword.apply{
                            requestFocus()
                            error = validation.password.msg
                        }
                    }
                }
            }
        }
    }

    private fun launchProgressCoroutine() {
        lifecycleScope.launch{
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegpageRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("TEST",it.data.toString())
                        binding.buttonRegpageRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e("TEST",it.msg.toString())
                        binding.buttonRegpageRegister.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun onRegisterClick() {
        binding.apply {
            buttonRegpageRegister.setOnClickListener {
                val user = User(
                    etRegFname.text.toString().trim(),
                    etRegLname.text.toString().trim(),
                    etRegEmail.text.toString().trim(),
                )
                val password = etRegPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }
    }


}