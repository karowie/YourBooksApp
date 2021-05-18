package com.example.yourbooks

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
    var listOfCurrentItems = ArrayList<Book>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bookLayoutManager = LinearLayoutManager(context)
        bookAdapter= BookAdapter(listOfItems, requireContext())
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_books, container, false)
        bookRecyclerView = fragmentView.findViewById(R.id.student_list)
        bookRecyclerView.setHasFixedSize(true)
        bookRecyclerView.layoutManager = LinearLayoutManager(context)

        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupLogoutClick()
        setupSearchClick(view)


        bookRecyclerView=student_list.apply {
            this.layoutManager = bookLayoutManager
            this.adapter = bookAdapter
        }

        currentUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Snackbar.make(requireView(), "Wystąpił problem z bazą danych.", Snackbar.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                listOfItems = ArrayList<Book>()
                if(p0.exists()){
                    for (h in p0.children){
                        //Log.d("Tutaj", h.getValue().toString() )
                        val model = h.getValue(Book::class.java)
                        listOfItems.add(model!!)
                    }
                    try{
                    val adapter = BookAdapter(listOfItems,context!!)
                    bookRecyclerView.setAdapter(adapter)
                    }
                    catch(e:Exception) { }
                }
            }
        })
    }

    private fun setupSearchClick(v:View) {
        imageButtonSearch.setOnClickListener {
            v.hideKeyboard()
            var phrase = editTextSearch.text.toString().trim()
            listOfCurrentItems = ArrayList<Book>()
            var tabFromBook:List<String>
            var tabFromPhrase:List<String>
            var isContain:Boolean
            if((phrase!=""))
            {
                for (item in listOfItems) {
                    tabFromBook = (item.author + " " + item.title).toUpperCase().split(" ")
                    tabFromPhrase = phrase.toUpperCase().split(" ")

                    isContain = false;
                    for(i in tabFromBook){
                        for (j in tabFromPhrase)
                        {
                            if(i==j) {
                                isContain = true
                                break;
                            }
                        }
                        if(isContain)
                            break
                    }
                    if(isContain)
                        listOfCurrentItems.add(item)
                }
            }
            else {
                    listOfCurrentItems = listOfItems
            }
            val adapter = BookAdapter(listOfCurrentItems,requireContext())
            bookRecyclerView.setAdapter(adapter)
        }
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


//    private fun setupLogoutClick() {
//        imageButtonLogOut.setOnClickListener {
//            if(fbAuth.currentUser!=null)
//            {
//                fbAuth.signOut()
//                LogOut()
//            }
//        }
//    }
}