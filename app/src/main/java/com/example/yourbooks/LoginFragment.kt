package com.example.yourbooks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEBUG="LOG_DEBUG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLoginClick()
        setUpRegistrationClick()
    }

    private fun setUpRegistrationClick() {
        loginRegisterButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment().actionId)
        }
    }

    private fun setUpLoginClick() {
        loginButton.setOnClickListener {
            val email = loginEmail.text?.trim().toString()
            val password = loginPassword.text?.trim().toString()

            fbAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {authRes ->
                    if(authRes.user!=null)
                        startApp()
                }
                .addOnFailureListener{exc ->
                    Snackbar.make(requireView(), "Coś poszło nie tak...", Snackbar.LENGTH_SHORT).show()
                    Log.d(LOG_DEBUG, exc.message.toString())
                }
        }

    }

}