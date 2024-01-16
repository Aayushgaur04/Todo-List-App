package com.aayush.todolistapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.aayush.todolistapp.R
import com.aayush.todolistapp.databinding.FragmentLoginBinding
import com.aayush.todolistapp.viewmodel.TaskViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel : TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val userName = sharedPreferences.getString("userName", "")
            if (!userName.isNullOrEmpty()) {
                // User has logged in before, navigate directly to HomeFragment
                val bundle = Bundle().apply {
                    putString("userName", userName)
                }
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
            }
        } else {
            // First-time login, proceed with normal login flow
            binding.button.setOnClickListener {
                val editTextContent = binding.editText.text.toString().trim()
                taskViewModel.setUserName(editTextContent)
                if (editTextContent.isNotEmpty()) {
                    with(sharedPreferences.edit()) {
                        putBoolean("isLoggedIn", true)
                        putString("userName", editTextContent)
                        apply()
                    }
                    val bundle = Bundle().apply {
                        putString("userName", editTextContent)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
                } else {
                    Toast.makeText(context, "Please Enter Your Name", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}