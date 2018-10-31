package com.example.xzl.note_taking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordsDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Notes.Node.TABLE_NAME + " (" +
            Notes.Node._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Notes.Node.COLUMN_NAME_TIME + " TEXT" + "," +
            Notes.Node.COLUMN_NAME_TEXT + " TEXT" +
   //         Notes.Node.COLUMN_NAME_Picture + " BINARY"+
           " )";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Notes.Node.TABLE_NAME;

    public WordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }

}
