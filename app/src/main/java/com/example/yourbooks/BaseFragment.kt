package com.example.yourbooks

import android.content.Intent
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    protected fun startApp()
    {
        val intent = Intent(requireContext(), MainActivity::class.java).apply{
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    protected fun LogOut()
    {
        val intent = Intent(requireContext(), RegistrationActivity::class.java).apply{
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }
}