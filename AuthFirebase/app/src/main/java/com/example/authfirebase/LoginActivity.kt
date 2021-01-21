package com.example.authfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class LoginActivity : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference
    private val CODE_IMG = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        config()

        findViewById<Button>(R.id.btn_cloud_storage).setOnClickListener {
            setGetIntent()
        }

        val createAccount = findViewById<TextView>(R.id.tv_create_account)
        createAccount.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        val loginButton = findViewById<Button>(R.id.btn_login)
        loginButton.setOnClickListener {
            getDataFields()
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            user.email?.let { callMain(user.uid, it) }
    }

    //cloud storage lesson
    fun config() {
        storageReference = FirebaseStorage.getInstance().getReference("prod_img")
    }

    fun setGetIntent() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Capture Image"), CODE_IMG)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG) {
            val uploadTask = data?.data?.let { storageReference.putFile(it) }
            uploadTask?.continueWithTask { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Chegando", Toast.LENGTH_SHORT).show()
                }
                storageReference.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val ivCloud = findViewById<ImageView>(R.id.iv_cloud_image)
                    val downloadUri = task.result
                    val url = downloadUri.toString().substring(0, downloadUri.toString().indexOf("&token"))
                    Log.i("Link Direto", url)
                    Picasso.get().load(url).into(ivCloud)
                }
            }
        }
    }









    fun callMain(key: String, email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("key", key)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    fun getDataFields() {
        val email = findViewById<TextInputEditText>(R.id.input_email_login).text.toString()
        val password = findViewById<TextInputEditText>(R.id.input_password_login).text.toString()
        val emailFilled = email.isNotEmpty()
        val passwordFilled = password.isNotEmpty()

        if (emailFilled && passwordFilled) {
            logInWithFirebase(email, password)
        } else {
            showMessage("Preencha todos os dados")
        }
    }

    fun logInWithFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser? = task.result?.user
                    val key = firebaseUser?.uid
                    val email = firebaseUser?.email.toString()
                    showMessage("Login realizado com sucesso!")
                    callMain(key!!, email)
                } else {
                    showMessage(task.exception?.message.toString())
                }
            }

    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun loginGoogle() {
        TODO("Is that very hard?")
    }
}