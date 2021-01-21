package com.example.authfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getValues()

        val logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener {
            logout()
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        callLogin()
    }

    fun getValues() {
        val extra = intent.extras
        if (extra != null) {
            val key = extra.getString("key")
            val email = extra.getString("email")
            showMessage("chave $key email: $email")
        }
    }

    fun callLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}