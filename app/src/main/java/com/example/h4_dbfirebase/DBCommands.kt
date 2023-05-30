package com.example.h4_dbfirebase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.sql.SQLException

class DBCommands(private val mCtx: Context) {
  private var mAdminSQLiteOpenHelper: AdminSQLiteOpenHelper? = null
  private var mSQLiteDatabase: SQLiteDatabase? = null

  @Throws(SQLException::class)
  fun open(): DBCommands {
    mAdminSQLiteOpenHelper = AdminSQLiteOpenHelper(mCtx)
    mSQLiteDatabase = mAdminSQLiteOpenHelper!!.writableDatabase
    return this
  }

  fun close() {
    if (mSQLiteDatabase != null) {
      mSQLiteDatabase!!.close()
    }
  }

  fun insertarUsuario(user: Usuario): Long {
    val initialValues = ContentValues()
    initialValues.put(KEY_ROWID, user.getUid())
    initialValues.put(KEY_NOMBRES, user.getFirstName())
    initialValues.put(KEY_APELLIDOS, user.getSurName())
    initialValues.put(KEY_EMAIL, user.getEmail())
    initialValues.put(KEY_PHONE, user.getPhone())
    if (mSQLiteDatabase == null) open()
    return mSQLiteDatabase!!.insert(TABLE_USER, null, initialValues)
  }

  fun eliminarUsuario(uid: String): Boolean {
    var nroRegistros = 0
    if (mSQLiteDatabase == null) open()
    nroRegistros = mSQLiteDatabase!!.delete(TABLE_USER, "uid='$uid'", null)
    return nroRegistros > 0
  }

  fun actualizarUsuario(user: Usuario): Long {
    var result: Long = 0
    val changeValues = ContentValues()
    changeValues.put(KEY_ROWID, user.getUid())
    changeValues.put(KEY_NOMBRES, user.getFirstName())
    changeValues.put(KEY_APELLIDOS, user.getSurName())
    changeValues.put(KEY_EMAIL, user.getEmail())
    changeValues.put(KEY_PHONE, user.getPhone())
    if (mSQLiteDatabase == null) open()
    result = mSQLiteDatabase!!.update(
      TABLE_USER,
      changeValues,
      "uid='" + user.getUid().toString() + "'",
      null
    ).toLong()
    return result
  }

  fun obtenerTodosRegistros(): Cursor? {
    var mCursor: Cursor? = null
    val columns = arrayOf(
      KEY_ROWID, KEY_NOMBRES, KEY_APELLIDOS,
      KEY_EMAIL, KEY_PHONE
    )
    mCursor = mSQLiteDatabase!!.query(TABLE_USER, columns, null, null, null, null, null)
    mCursor?.moveToFirst()
    return mCursor
  }

  fun getUid(c: Cursor): String {
    return c.getString(0)
  }

  fun getFirstName(c: Cursor): String {
    return c.getString(1)
  }

  fun getLastName(c: Cursor): String {
    return c.getString(2)
  }

  fun getEmail(c: Cursor): String {
    return c.getString(3)
  }

  fun getPhone(c: Cursor): String {
    return c.getString(4)
  }

  companion object {
    private const val KEY_ROWID = "uid"
    private const val KEY_NOMBRES = "nombres"
    private const val KEY_APELLIDOS = "apellidos"
    private const val KEY_EMAIL = "email"
    private const val KEY_PHONE = "phone"
    private const val DATABASE_NAME = "DBUsuarios"
    private const val DATABASE_VERSION = 1
    private const val TABLE_USER = "Usuarios"
  }
}