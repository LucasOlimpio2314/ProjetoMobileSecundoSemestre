package com.projeto.project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.projeto.project.databinding.ActivityConfiguracoesBinding
import com.projeto.project.databinding.ActivityHomeBinding

class Configuracoes : AppCompatActivity() {

    private val binding by lazy {
        ActivityConfiguracoesBinding.inflate(layoutInflater)
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
                    binding.textmostrarNome.text = nome.toString();
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
        inicializandoToolbarPrincipal()

        binding.btnHome.setOnClickListener {
            finish()
        }
        binding.btnNome.setOnClickListener {
            startActivity(Intent(this, UpdateDados ::class.java))
        }
        binding.btnDeslogar.setOnClickListener {
            autenticacao.signOut()
            startActivity(Intent(this, MainActivity :: class.java))
        }

        val email = autenticacao.currentUser?.email
        binding.textMostrarEmail.text = "$email - CoffeBook";
        pucharInformacoesUsuario()

    }

    private fun inicializandoToolbarPrincipal() {
        binding.tbPrincipal.inflateMenu(R.menu.top_bar)
    }
}