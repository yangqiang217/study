package com.example.aidl;

import com.example.aidl.Book;
interface IOnNewBookArrivedListener {
     void onNewBook(in Book book);
}