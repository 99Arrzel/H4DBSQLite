package com.example.h4_dbfirebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper : SQLiteOpenHelper {
  constructor(context: Context?, name: String?, factory: CursorFactory?, version: Int) : super(
    context,
    DATABASE_NAME,
    factory,
    DATABASE_VERSION
  ) {
  }

  constructor(context: Context?) : super(context, DATABASE_NAME, null, DATABASE_VERSION) {}

  override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
    sqLiteDatabase.execSQL(DATABASE_CREATE)
    //insertSomeUsers(sqLiteDatabase);
  }

  override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
  private fun insertSomeUsers(db: SQLiteDatabase) {
    db.execSQL(TBUSER_INSERT + " ('User ID 1','Nombres User 1 ','Apellidos User 1','email@user1.com','123 456564')")
    db.execSQL(TBUSER_INSERT + " ('User ID 2','Nombres User 2 ','Apellidos User 2','email@user2.com','234 565657')")
    db.execSQL(TBUSER_INSERT + " ('User ID 3','Nombres User 3 ','Apellidos User 3','email@user3.com','345 465657')")
    db.execSQL(TBUSER_INSERT + " ('User ID 4','Nombres User 4 ','Apellidos User 4','email@user4.com','456 565678')")
    db.execSQL(TBUSER_INSERT + " ('User ID 5','Nombres User 5 ','Apellidos User 5','email@user5.com','567 674678')")
  }

  companion object {
    private const val KEY_ROWID = "uid"
    private const val KEY_NOMBRES = "nombres"
    private const val KEY_APELLIDOS = "apellidos"
    private const val KEY_EMAIL = "email"
    private const val KEY_PHONE = "phone"
    private const val DATABASE_NAME = "DBUsuarios"
    private const val TABLE_USER = "Usuarios"
    private const val DATABASE_VERSION = 1
    private const val DATABASE_CREATE = "CREATE TABLE if not exists " + TABLE_USER + " (" +
            KEY_ROWID + " TEXT PRIMARY KEY, " +
            KEY_NOMBRES + " TEXT, " +
            KEY_APELLIDOS + " TEXT, " +
            KEY_EMAIL + " TEXT, " +
            KEY_PHONE + " TEXT, " +
            " UNIQUE (" + KEY_EMAIL + "));"
    private const val TBUSER_INSERT = "INSERT INTO " + TABLE_USER + " (" +
            KEY_ROWID + " ," +
            KEY_NOMBRES + " ," +
            KEY_APELLIDOS + " ," +
            KEY_EMAIL + " ," +
            KEY_PHONE + ")" +
            " VALUES"
  }
}



























