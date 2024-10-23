package com.projeto.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.projeto.project.databinding.ActivityCadastroBinding
import com.projeto.project.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    lateinit var buttonFechar : Button

    private val binding by lazy {
        ActivityLoginBinding.inflate( layoutInflater)
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        buttonFechar = findViewById(R.id.buttonFechar)
        buttonFechar.setOnClickListener {
        finish()
        }

        binding.buttonEntrar.setOnClickListener {
            LogarUsuario()
        }

    }

    private fun LogarUsuario() {
        val email : TextView = findViewById(R.id.textEmail)
        val senha : TextView = findViewById(R.id.textSenha)
        val emailUsuario = email.text.toString()
        val senhaUsuario = senha.text.toString()

        if (emailUsuario.isNotEmpty() && senhaUsuario.isNotEmpty()){
            autenticacao.signInWithEmailAndPassword(
                emailUsuario,senhaUsuario
            ).addOnSuccessListener { authResult ->
                var intent = Intent(this,InformacoeCadastro ::class.java)
                startActivity(intent)
            }.addOnFailureListener { exception ->
                val erro = exception.message
                binding.mensagemErro.text = "error: $erro"
            }
        }
    }
}

