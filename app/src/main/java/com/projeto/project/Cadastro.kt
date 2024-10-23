package com.projeto.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.projeto.project.databinding.ActivityCadastroBinding
import com.projeto.project.databinding.ActivityMainBinding

class Cadastro : AppCompatActivity() {

    private lateinit var buttonFechar : Button

    private val binding by lazy {
        ActivityCadastroBinding.inflate( layoutInflater)
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




        buttonFechar = findViewById(R.id.buttonFecharC)
        buttonFechar.setOnClickListener {
            finish()
        }

        binding.btnCadastro.setOnClickListener {
            cadastroUsuario()
        }

    }

    private fun cadastroUsuario() {
        // Pegando o valor do email do textview
        val email : TextView = findViewById(R.id.email)
        val emailConfirmar : TextView = findViewById(R.id.ConEmail)
        val emailCon = emailConfirmar.text.toString()
        val emailU = email.text.toString()

        //pegando o valor da senha
        val senha : TextView = findViewById(R.id.senha)
        val senhaConfirmar : TextView = findViewById(R.id.ConSenha)
        val senhaCon = senhaConfirmar.text.toString()
        val senhaU = senha.text.toString()

        if (emailU.isNotEmpty() && emailU == emailCon && senhaU.isNotEmpty() && senhaU == senhaCon) {
            val autenticacao = FirebaseAuth.getInstance()
            autenticacao.createUserWithEmailAndPassword(
                emailU, senhaU
            ).addOnSuccessListener { authResult ->
                var intent = Intent(this,Login ::class.java)
                startActivity(intent)
            }.addOnFailureListener { exception ->
                val erro = exception.message
                binding.teste.text = "error: $erro"
            }


        }else{
            binding.teste.text = "Campo não preenchido ou email/senha inserido errado"
        }
    }

    private fun exibirMensagem(texto: String) {
        Toast.makeText(this,texto, Toast.LENGTH_LONG).show()
    }
}