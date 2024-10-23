package com.projeto.project

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projeto.project.databinding.ActivityConfiguracoesBinding
import com.projeto.project.databinding.ActivityUpdateDadosBinding

class UpdateDados : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateDadosBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private  val bancoDeDados by lazy {
        FirebaseFirestore.getInstance()
    }
    private fun pucharInformacoesUsuario() {
        val tabela_ref = bancoDeDados.collection("Usuario")
        val idUsusario = autenticacao.currentUser?.uid

        if (idUsusario != null){
            tabela_ref.document(idUsusario).addSnapshotListener { value, error ->
                val dados = value?.data
                if (dados != null){
                    val nome = dados["Nome"]
                    val nomeU = nome.toString()
                    binding.editTextDado.hint = nomeU
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pucharInformacoesUsuario()

        binding.btnVoltar.setOnClickListener {
            finish()
        }
        binding.btnUpdate.setOnClickListener {
            updateNome()
        }


    }

    private fun updateNome() {
        val intent = Intent(this, Configuracoes ::class.java)
        val nome : TextView = findViewById(R.id.editTextDado)
        val nomeU = nome.text.toString()
        val dados = mapOf(
            "Nome" to nomeU
        )
        val tabela_ref = bancoDeDados.collection("Usuario")
        val idUsusario = autenticacao.currentUser?.uid

        if (idUsusario != null && nomeU.isNotEmpty()){
            tabela_ref.document(idUsusario).update(dados)
                .addOnSuccessListener {
                    startActivity(intent)
                }
        }
    }
}