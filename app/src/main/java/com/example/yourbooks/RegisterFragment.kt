package com.example.yourbooks

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
        setupSingUpClick(view)
    }

    private fun setupSingUpClick(v:View) {
        registerButton.setOnClickListener {
            val email = registerEmail.text?.trim().toString()
            val pass = registerPassword.text?.trim().toString()
            val repeatedPass = registerPasswordRepeated.text?.trim().toString()

            v.hideKeyboard()

            if(email=="")
                Snackbar.make(requireView(), "Wprowadź mail.", Snackbar.LENGTH_SHORT).show()
            else if (pass == "")
                Snackbar.make(requireView(), "Wprowadź hasło.", Snackbar.LENGTH_SHORT).show()
            else if (repeatedPass=="")
                Snackbar.make(requireView(), "Powtórz hasło.", Snackbar.LENGTH_SHORT).show()
            else if (pass==repeatedPass)
            {
                if(pass.length<6)
                    Snackbar.make(requireView(), "Hasło musi składać się z conajmniej 6 znaków.", Snackbar.LENGTH_SHORT).show()
                else {
                    fbAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnSuccessListener { authRes ->
                                if (authRes != null)
                                    startApp()

                            }
                            .addOnFailureListener { exc ->
                                if(exc.message.toString()=="The email address is badly formatted.")
                                    Snackbar.make(requireView(), "Błędny format maila.", Snackbar.LENGTH_SHORT).show()
                                else
                                    Snackbar.make(requireView(), "Coś poszło nie tak...", Snackbar.LENGTH_SHORT).show()
                                Log.d(REG_DEBUG, exc.message.toString())
                            }
                }
            }
            else
            {

                Snackbar.make(requireView(), "Podane hasła różnią się.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}