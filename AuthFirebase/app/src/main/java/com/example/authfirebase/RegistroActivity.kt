package com.example.authfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val button = findViewById<Button>(R.id.btn_create)
        button.setOnClickListener {
            getDataFields()
        }
    }

    //Captura os dados digitados nos campos
    fun getDataFields() {
        val email = findViewById<TextInputEditText>(R.id.input_email).text.toString()
        val password = findViewById<TextInputEditText>(R.id.input_password).text.toString()
        val emailFilled = email.isNotEmpty()
        val passwordFilled = password.isNotEmpty()

        if (emailFilled && passwordFilled) {
            sendDataToFirebase(email, password)
        } else {
            showMessage("Preencha todos os dados")
        }
    }

    //    Envia cadastro para o firebase
    fun sendDataToFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser? = task.result?.user
                    val key = firebaseUser?.uid
                    val email = firebaseUser?.email.toString()
                    showMessage("Cadastro realizado com sucesso!")
                    callMain(key!!, email)
                } else {
                    showMessage(task.exception?.message.toString())
                }
            }

    }

    fun callMain(key: String, email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("key", key)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}