package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context mContext;
    public static final  String CREATE_USER="create table userInfo("
            +"id integer primary key autoincrement,"
            +"username varchar(20),"
            +"password varchar(20))";
    //带全部参数的构造函数，此构造函数必不可少
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists userInfo");
        db.execSQL("drop table if exists userInformation");
        onCreate(db);
    }
}
