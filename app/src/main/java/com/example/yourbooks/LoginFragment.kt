package com.example.yourbooks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        setUpLoginClick(view)
        setUpRegistrationClick()
    }

    private fun setUpRegistrationClick() {
        loginRegisterButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment().actionId)
        }
    }

    private fun setUpLoginClick(v:View) {
        loginButton.setOnClickListener {
            val email = loginEmail.text?.trim().toString()
            val password = loginPassword.text?.trim().toString()

            if(email=="")
            {
                v.hideKeyboard()
                Snackbar.make(requireView(), "Wprowadź email.", Snackbar.LENGTH_SHORT).show()
            }
            else if(password=="")
            {
                v.hideKeyboard()
                Snackbar.make(requireView(), "Wprowadź hasło.", Snackbar.LENGTH_SHORT).show()
            }
            else {
                fbAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authRes ->
                            if (authRes.user != null)
                                startApp()
                        }
                        .addOnFailureListener { exc ->
                            v.hideKeyboard()
                            Snackbar.make(requireView(), "Nie ma takiego użytkownika w bazie. Sprawdź login i hasło.", Snackbar.LENGTH_SHORT).show()
                            Log.d(LOG_DEBUG, exc.message.toString())
                        }
            }
        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}