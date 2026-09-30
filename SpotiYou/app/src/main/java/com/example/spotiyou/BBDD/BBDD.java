package com.example.spotiyou.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BBDD extends SQLiteOpenHelper {

    String SQLcrearTabla = "CREATE TABLE IF NOT EXISTS usuarioBBDD(codigo INTEGER PRIMARY KEY AUTOINCREMENT, Nombre TEXT, Imagen TEXT, Estado INTEGER, Token TEXT, RefreshToken TEXT);";

    public BBDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLcrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarioBBDD");
        db.execSQL(SQLcrearTabla);
    }
}
