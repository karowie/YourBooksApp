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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_book.*
import kotlinx.android.synthetic.main.fragment_edit_book.*
import kotlinx.android.synthetic.main.fragment_edit_book.editTextAuthor
import kotlinx.android.synthetic.main.fragment_edit_book.editTextTitle
import kotlinx.android.synthetic.main.fragment_edit_book.spinnerIsRead
import kotlinx.android.synthetic.main.fragment_edit_book.view.*
import kotlinx.android.synthetic.main.fragment_edit_book.view.spinnerIsRead

class EditBookFragment : Fragment() {

    var isRead = ""
    var id =""
    val uid = FirebaseAuth.getInstance().uid?:""
    val currentUser = FirebaseDatabase.getInstance().getReference("books/$uid")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_book, container, false)

        id = arguments?.getString("Id")?:""
        view.editTextAuthor.setText(arguments?.getString("Author")?:"")
        view.editTextTitle.setText(arguments?.getString("Title")?:"")
        isRead = arguments?.getString("IsRead")?:""
        view.editTextComment.setText(arguments?.getString("Comment")?:"")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCancelButton(view)
        setSaveButton(view)

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
        if(isRead!="Przeczytane")
            spinnerIsRead.setSelection(1)

    }

    private fun setCancelButton(v: View)
    {
        buttonCancel.setOnClickListener {
            v.hideKeyboard()
            goBackToBooksFragment()
        }
    }

    private fun setSaveButton(v: View)
    {
        buttonSave.setOnClickListener {
            val author = editTextAuthor.text.toString()
            val title = editTextTitle.text.toString()
            val read = spinnerIsRead.spinnerIsRead.getSelectedItem().toString()
            val comment = editTextComment.text.toString()

            val map = HashMap<String,Any>()
            map.put("author", author)
            map.put("title", title)
            map.put("read", read)
            map.put("comment", comment)

            try
            {
                currentUser.child(id).updateChildren(map)
                v.hideKeyboard()
                goBackToBooksFragment()
            }
            catch(e:Exception)
            {
                Snackbar.make(requireView(), "Coś poszło nie tak...", Snackbar.LENGTH_SHORT).show()
            }


        }
    }

    private fun goBackToBooksFragment()
    {
        var activity=context as AppCompatActivity
        var booksFragment = BooksFragment()
        activity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, booksFragment)
            commit()
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}