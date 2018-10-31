package com.example.xzl.note_taking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Notes {
    public Notes(){
    }
    public static abstract class Node implements BaseColumns {
        public static final String TABLE_NAME="notes";
        public static final String COLUMN_NAME_TIME="time";//列：
        public static final String COLUMN_NAME_TEXT="text";//列：
       // public static final String COLUMN_NAME_Picture="picture";//
    }
}
