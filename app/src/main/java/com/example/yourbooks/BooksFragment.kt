package com.example.yourbooks

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourbooks.Model.Book
import com.example.yourbooks.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_books.*
import com.google.firebase.database.*



class BooksFragment : BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val uid = FirebaseAuth.getInstance().uid?:""
    private val currentUser = FirebaseDatabase.getInstance().getReference("books/$uid")

    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookLayoutManager: LinearLayoutManager
    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var fragmentView: View
    var listOfItems = ArrayList<Book>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bookLayoutManager = LinearLayoutManager(context)
        bookAdapter= BookAdapter(listOfItems, requireContext())
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_books, container, false)
        bookRecyclerView = fragmentView?.findViewById(R.id.student_list)
        bookRecyclerView?.setHasFixedSize(true)
        bookRecyclerView?.layoutManager = LinearLayoutManager(context)

        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLogoutClick()
        setupAddButton()

        context?.let { context ->
            val list = mutableListOf(
                "Przeczytane",
                "Nie przeczytane"
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

        bookRecyclerView=student_list.apply {
            this.layoutManager = bookLayoutManager
            this.adapter = bookAdapter
        }

        currentUser?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                listOfItems = ArrayList<Book>()
                if(p0!!.exists()){
                    for (h in p0.children){
                        Log.d("Tutaj", h.getValue().toString() )
                        val model = h.getValue(Book::class.java)
                        listOfItems?.add(model!!)
                    }
                    val adapter = BookAdapter(listOfItems,context!!)
                    bookRecyclerView?.setAdapter(adapter)
                }
            }
        })



    }

    private fun setupAddButton() {
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
                    editTextAuthor.setText("")
                    editTextTitle.setText("")
                    Snackbar.make(requireView(), "Dodałeś nową książkę.", Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }



    private fun setupLogoutClick() {
        imageButtonLogOut.setOnClickListener {
            if(fbAuth.currentUser!=null)
            {
                fbAuth.signOut()
                LogOut()
            }
        }
    }
}