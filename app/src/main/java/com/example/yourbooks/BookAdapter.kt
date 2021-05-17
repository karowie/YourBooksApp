package com.example.yourbooks

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourbooks.Model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BookAdapter (private val dataArray: ArrayList<Book>, private val con: Context):RecyclerView.Adapter<BookAdapter.BookHolder>() {

    inner class BookHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_row_book, parent, false)
        return BookHolder(view)
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        var textViewTitle = holder.itemView.findViewById<TextView>(R.id.textViewTitle)
        var textViewAuthor = holder.itemView.findViewById<TextView>(R.id.textViewAuthor)
        var textViewComment = holder.itemView.findViewById<TextView>(R.id.textViewComment)
        var textViewIsRead = holder.itemView.findViewById<TextView>(R.id.textViewIsReadText)

        var delButton = holder.itemView.findViewById<ImageButton>(R.id.imageButtonDel)

        var card = holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.card)

        val uid = FirebaseAuth.getInstance().uid?:""
        val currentUser = FirebaseDatabase.getInstance().getReference("books/$uid")

        val book = dataArray[position]

        textViewTitle.text = book.title
        textViewAuthor.text = book.author
        textViewComment.text = book.comment

        if (book.read == "Przeczytane") {
            textViewIsRead.text = "Przeczytane"
            textViewIsRead.setTextColor(Color.parseColor("#fafffb"))


        } else {
            textViewIsRead.text = "Nie przeczytane"
            textViewIsRead.setTextColor(Color.parseColor("#c8e6c9"))
        }

        delButton.setOnClickListener {
            currentUser.child(book.id).removeValue()
        }
    }
}