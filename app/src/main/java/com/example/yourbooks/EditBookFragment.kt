package com.example.yourbooks

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_edit_book.*
import kotlinx.android.synthetic.main.fragment_edit_book.view.*

class EditBookFragment : Fragment() {

    var isRead = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_book, container, false)

        view.editTextAuthor.setText(arguments?.getString("Author")?:"")
        view.editTextTitle.setText(arguments?.getString("Title")?:"")
        isRead = arguments?.getString("IsRead")?:""
        view.editTextComment.setText(arguments?.getString("Comment")?:"")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCancelButton()

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

    private fun setCancelButton()
    {
        buttonCancel.setOnClickListener {
            var activity=context as AppCompatActivity
            var booksFragment = BooksFragment()
            activity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, booksFragment)
                commit()
            }

        }
    }

}