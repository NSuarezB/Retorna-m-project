package com.example.retornam20.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "objecte.db";
    public static final String TABLE_NAME = "objecte";
    public static final String ID = "Id";
    public static final String NOM_OBJECTE = "NomObjecte";
    public static final String DESCRIPCIO = "Descripcion";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , 1 );
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (Id INTEGER PRIMARY KEY AUTOINCREMENT,NomObjecte TEXT,Descripcio TEXT) " );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
