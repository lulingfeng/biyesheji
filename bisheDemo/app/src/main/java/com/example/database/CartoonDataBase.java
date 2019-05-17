package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartoonDataBase extends SQLiteOpenHelper {
    Context mContext;
    public static final  String CREATE_USER="create table cartoonInfo("
            +"id integer primary key autoincrement,"
            +"cartoonLink varchar(50),"
            +"cartoonId varchar(20))";
    public CartoonDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        db.execSQL("drop table if exists cartoonData");
        onCreate(db);
    }
}
