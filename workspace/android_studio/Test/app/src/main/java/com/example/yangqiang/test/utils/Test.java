package com.example.yangqiang.test.utils;

import android.util.ArrayMap;
import android.util.SparseArray;

public class Test {

    public static void map() {
        SparseArray<String> array = new SparseArray<>();
        array.put(1, "1");
        array.get(1);

        ArrayMap<Integer, String> map = new ArrayMap<>();
        map.put(1, "1");
        map.get(1);
    }
}
