package com.example.h4_dbfirebase

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.util.UUID

class MainActivity : AppCompatActivity(), OnUsuarioClickListener {

    var usuarioNombres: EditText? = null
    var usuarioApellidos: EditText? = null
    var usuarioEmail: EditText? = null
    var usuarioTelefono: EditText? = null
    private lateinit var rvUsuarios: RecyclerView
    //FireBase
    var usuarios: MutableList<Usuario> = ArrayList<Usuario>()
    var UserSelected: String? = null
    //var databaseReference: DatabaseReference? = null

    //var firebaseDatabase : FirebaseDatabase? = null

    var UserIDSelected: String? = null

    private var mDBCommands: DBCommands? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuarioNombres = findViewById(R.id.txtNombres)
        usuarioApellidos = findViewById(R.id.txtApellidos)
        usuarioEmail = findViewById(R.id.txtEmail)
        usuarioTelefono = findViewById(R.id.txtPhone)
        rvUsuarios = findViewById(R.id.rvDatosUsuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(this)


        mDBCommands = DBCommands(this)
        mDBCommands!!.open()
        //initFirebase()
        listaUsuariosData()
    }
    //Creación del Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun listaUsuariosData(){
        usuarios.clear()
        cursor = mDBCommands!!.obtenerTodosRegistros()
        if (cursor!!.moveToFirst()){
            do {
                val usuario = Usuario()
                usuario.setUid(cursor!!.getString(0))
                usuario.setFirstName(cursor!!.getString(1) + " ")
                usuario.setSurName(cursor!!.getString(2))
                usuario.setEmail(cursor!!.getString(3))
                usuario.setPhone(cursor!!.getString(4))
                usuarios.add(usuario)
            }while (cursor!!.moveToNext())
        }
        rvUsuarios!!.adapter = UserAdapter(this@MainActivity, usuarios, this@MainActivity)
    }
    //Implementación del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val nombres = usuarioNombres!!.text.toString()
        val apellidos = usuarioApellidos!!.text.toString()
        val email = usuarioEmail!!.text.toString()
        val telefono = usuarioTelefono!!.text.toString()

        when (item.itemId){
            R.id.icon_add -> {
                if (nombres.isNullOrEmpty()||apellidos.isNullOrEmpty() || (email.isNullOrEmpty() || !correoValido(email) || uniqueEmail(email.trim()) ) || telefono.isNullOrEmpty() ) {
                    validateData()
                } else {
                    val usuario = Usuario()
                    usuario.setUid(UUID.randomUUID().toString())
                    usuario.setFirstName(nombres.trim())
                    usuario.setSurName(apellidos.trim())
                    usuario.setEmail(email.trim())
                    usuario.setPhone(telefono.trim())

                    //grabar a Firebase linea a completar
                    //var res = databaseReference!!.child("Usuario").child(usuario.getUid()!!).setValue(usuario)
                    //println(res)
                    var res = mDBCommands?.insertarUsuario(usuario)
                    if(res!! > 0) {
                        Toast.makeText(this, "Usuario Añadido", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Error al añadir usuario", Toast.LENGTH_SHORT).show()
                    }
                    cleanData()
                    listaUsuariosData()
                }
            }

            R.id.icon_edit -> {
                if(UserIDSelected == null) {
                    Toast.makeText(this, "Seleccione un usuario", Toast.LENGTH_SHORT).show()
                }else {
                    val usuarioEdited = Usuario()
                    usuarioEdited.setUid(UserIDSelected!!)
                    usuarioEdited.setFirstName(usuarioNombres!!.text.toString())
                    usuarioEdited.setSurName(usuarioApellidos!!.text.toString())
                    usuarioEdited.setEmail(usuarioEmail!!.text.toString())
                    usuarioEdited.setPhone(usuarioTelefono!!.text.toString())
                    //Grabar a firebase
                    //databaseReference!!.child("Usuario").child(usuarioEdited.getUid()!!)
                    //    .setValue(usuarioEdited)
                    //Grabar a SQLite
                    var res = mDBCommands?.actualizarUsuario(usuarioEdited)
                    if(res!! > 0) {
                        Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                    }
                    //Ahora limpiamos
                    cleanData()
                    listaUsuariosData()
                }
            }

            R.id.icon_delete -> {
                if(UserIDSelected == null) {
                    Toast.makeText(this, "Seleccione un usuario", Toast.LENGTH_SHORT).show()
                }else {
                    var usuarioDelete = Usuario()
                    usuarioDelete.setUid(UserIDSelected!!)
                    //databaseReference!!.child("Usuario").child(usuarioDelete.getUid()!!)
                    //    .removeValue()
                    var res = mDBCommands?.eliminarUsuario(usuarioDelete.getUid()!!)
                    if(res!!) {
                        Toast.makeText(this, "Usuario Eliminado", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
                    }
                    cleanData()
                    listaUsuariosData()
                }
            }
        }
        return true
    }
    //Borrar campos
    private fun cleanData() {
        usuarioNombres!!.setText("")
        usuarioApellidos!!.setText("")
        usuarioEmail!!.setText("")
        usuarioTelefono!!.setText("")
        UserIDSelected = null
        usuarioNombres!!.requestFocus()
    }

    //Validar datos
    private fun validateData(){
        val nombres = usuarioNombres!!.text.toString()
        val apellidos = usuarioApellidos!!.text.toString()
        val email = usuarioEmail!!.text.toString()
        val telefono = usuarioTelefono!!.text.toString()

        if (nombres.isNullOrEmpty()) {
            usuarioNombres!!.error = "Obligatorio"
        }else if (apellidos.isNullOrEmpty()) {
            usuarioApellidos!!.error = "Obligatorio"
        }else if (email.isNullOrEmpty() || !correoValido(email)) {
            usuarioEmail!!.error = "El correo es obligatorio y debe ser válido"
        }else if (telefono.isNullOrEmpty()) {
            usuarioTelefono!!.error = "Obligatorio"
        }
        if(uniqueEmail(email)){
            usuarioEmail!!.error = "El correo ya existe"
        }
    }
    fun correoValido(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun uniqueEmail(email: String): Boolean{
        return usuarios.find { it.getEmail() == email } != null
    }

    override fun OnItemClick(usuario: Usuario) {
        UserIDSelected = usuario.getUid()
        //print
        println(UserIDSelected)
        Toast.makeText(this, "Seleccionado: ${usuario.getFirstName()}", Toast.LENGTH_SHORT).show()
        usuarioNombres!!.setText(usuario.getFirstName())
        usuarioApellidos!!.setText(usuario.getSurName())
        usuarioEmail!!.setText(usuario.getEmail())
        usuarioTelefono!!.setText(usuario.getPhone())
    }
    override fun onDestroy() {
        super.onDestroy()
        cursor!!.close()
        mDBCommands!!.close()
    }
}