package com.example.aidl;

import com.example.aidl.Book;
interface IOnNewBookArrivedListener {
     void OnNewBookArrivedListener(in Book book);
}