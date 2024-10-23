package com.projeto.project

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projeto.project.databinding.ActivityCadastroBinding
import com.projeto.project.databinding.ActivityInformacoeCadastroBinding

class InformacoeCadastro : AppCompatActivity() {



    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private  val bancoDeDados by lazy {
        FirebaseFirestore.getInstance()
    }
    private val binding by lazy {
        ActivityInformacoeCadastroBinding.inflate( layoutInflater)
    }






    private fun verificarUsuario() {
        val tabela_ref = bancoDeDados.collection("Usuario")
        val idUsusario = autenticacao.currentUser?.uid

        if (idUsusario != null){
            tabela_ref.document(idUsusario).addSnapshotListener { value, error ->
                val dados = value?.data
                if (dados != null){
                    val nome = dados["Nome"]
                    if (nome != null){
                        val intent = Intent(this,Home ::class.java)
                        startActivity(intent)
                    }
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

        binding.btnContinuar.setOnClickListener {
            cadastroInformacoes()
        }
        verificarUsuario()
    }

    private fun cadastroInformacoes() {
        val nomeUsuario : TextView = findViewById(R.id.editTextNomeUsuario)
        val nomeU = nomeUsuario.text.toString()
        val idUsusario = autenticacao.currentUser?.uid
        val dados = mapOf(
            "Nome" to nomeU
        )
        val tabela_ref = bancoDeDados.collection("Usuario")


        if (idUsusario != null && nomeU.isNotEmpty()) {
            tabela_ref.document(idUsusario).set(dados)
                .addOnSuccessListener {
                    startActivity(intent)
                }.addOnFailureListener { exception ->
                    val erro = exception.message
                    binding.textView14.text = "error: $erro"
                }
        }
    }
}