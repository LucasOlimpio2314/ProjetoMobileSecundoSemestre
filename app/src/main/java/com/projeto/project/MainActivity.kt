package com.projeto.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var buttonAcesso : Button
    lateinit var buttonCadastro : Button

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuario = autenticacao.currentUser
        if (usuario != null){
            var intent = Intent(this,Home ::class.java)
            startActivity(intent)
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonAcesso = findViewById(R.id.buttonAcessar)
        buttonAcesso.setOnClickListener {
            var intent = Intent(this,Login ::class.java)
            startActivity(intent)
        }

        buttonCadastro = findViewById(R.id.buttonCadastro)
        buttonCadastro.setOnClickListener {
            var intent = Intent(this,Cadastro ::class.java)
            startActivity(intent)
        }




    }
}