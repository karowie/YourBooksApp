package com.example.yourbooks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.yourbooks.Model.Book
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_books.*

class MainActivity : AppCompatActivity() {
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val booksFragment = BooksFragment()
        val addBookFragment = AddBookFragment()

        //makeCurrentFragment(booksFragment)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.books ->{
                    this.setTitle("Moja mała księgarnia")
                    makeCurrentFragment(booksFragment)
                }
                R.id.addBook -> {
                    this.setTitle("Tutaj możesz dodać książkę")
                    makeCurrentFragment(addBookFragment)}
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_logOut -> logoutClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeCurrentFragment(f: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, f)
            commit()
        }
    }

    private fun logoutClick() {
            if(fbAuth.currentUser!=null)
            {
                fbAuth.signOut()
                val intent = Intent(baseContext, RegistrationActivity::class.java).apply{
                    flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            }
    }

//    override fun passData(book: Book) {
//        val budle = Bundle()
//        budle.putParcelable(book)
//
//        val transaction = this.supportFragmentManager.beginTransaction()
//        val editBookFragment = EditBookFragment()
//
//        editBookFragment.arguments = budle
//        transaction.replace()
//    }


}