package com.example.yourbooks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseFragment() {

    private val REG_DEBUG = "REG_DEBUG"
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSingUpClick()
    }

    private fun setupSingUpClick() {
        registerButton.setOnClickListener {
            val email = registerEmail.text?.trim().toString()
            val pass = registerPassword.text?.trim().toString()
            val repeatedPass = registerPasswordRepeated.text?.trim().toString()

            if(pass==repeatedPass)
            {
                fbAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnSuccessListener {authRes ->
                        if(authRes!=null)
                            startApp()

                    }
                    .addOnFailureListener{exc ->
                        Snackbar.make(requireView(), "Coś poszło nie tak...", Snackbar.LENGTH_SHORT).show()
                        Log.d(REG_DEBUG, exc.message.toString())
                    }
            }
        }
    }
}