package com.example.yourbooks

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.yourbooks.Model.Book
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_book.*

class AddBookFragment : Fragment() {

    private val uid = FirebaseAuth.getInstance().uid?:""
    private val currentUser = FirebaseDatabase.getInstance().getReference("books/$uid")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddButton(view)

        context?.let { context ->
            val list = mutableListOf(
                "Przeczytane",
                "Nieprzeczytane"
            )
            val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_dropdown_item,
                list
            ) {
                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    view.hideKeyboard()
                    val view: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView
                    view.setTypeface(Typeface.MONOSPACE, Typeface.BOLD)
                    view.setTextColor(Color.parseColor("#FF000000"))

                    if (position == spinnerIsRead.selectedItemPosition) {
                        view.background = ColorDrawable(Color.parseColor("#c8e6c9"))
                    }
                    return view
                }

            }
            spinnerIsRead.adapter = adapter
        }
    }

    private fun setupAddButton(v:View) {
        buttonAdd.setOnClickListener {

            if(editTextAuthor.text.toString()=="")
            {
                Snackbar.make(requireView(), "Musisz podać autora", Snackbar.LENGTH_SHORT).show()
            }
            else if(editTextTitle.text.toString()=="")
            {
                Snackbar.make(requireView(), "Musisz podać tytuł", Snackbar.LENGTH_SHORT).show()
            }
            else{
                var author = editTextAuthor.text.toString().trim()
                var title = editTextTitle.text.toString().trim()

                val id = currentUser.push().key?:""

                val book = Book(id, author,title,spinnerIsRead.getSelectedItem().toString(),"")

                currentUser.child(id).setValue(book).addOnCompleteListener{
                    v.hideKeyboard()
                    editTextAuthor.setText("")
                    editTextTitle.setText("")
                    Snackbar.make(requireView(), "Dodałeś nową książkę.", Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}