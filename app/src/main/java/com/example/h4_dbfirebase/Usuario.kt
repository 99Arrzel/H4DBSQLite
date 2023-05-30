package com.example.h4_dbfirebase

class Usuario {
    private var uid: String? = null
    private var nombres: String? = null
    private var apellidos: String? = null
    private var email: String? = null
    private var telefono: String? = null
    //constructor
    fun Usuario() {}

    fun getUid(): String? {
        return uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getFirstName(): String? {
        return nombres
    }

    fun setFirstName(nombres: String) {
        this.nombres = nombres
    }

    fun getSurName(): String? {
        return apellidos
    }

    fun setSurName(apellidos: String) {
        this.apellidos = apellidos
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getPhone(): String? {
        return telefono
    }

    fun setPhone(telefono: String) {
        this.telefono = telefono
    }


    override fun toString(): String {
        return "$nombres" + "$apellidos"

    }

}