package com.example.yourbooks

import com.example.yourbooks.Model.Book

interface Comunicator {
    fun passData(book: Book)
}