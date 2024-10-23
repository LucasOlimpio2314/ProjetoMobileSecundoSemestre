package com.projeto.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.projeto.project.databinding.ActivityPostBinding
import com.projeto.project.databinding.ActivityUpdateDadosBinding

class Post : AppCompatActivity() {

    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    private val bancoDeDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private var uriArquivo: Uri? = null

    private val pegarPdf = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.textNomedoArq.text = uri.toString()
            uriArquivo = uri
        } else {
            Toast.makeText(this, "Nenhum arquivo selecionado", Toast.LENGTH_LONG)
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

        binding.btnVoltarH.setOnClickListener {
            finish()
        }

        binding.btnSelecionararquivo.setOnClickListener {
            pegarPdf.launch("application/pdf")
        }

        binding.buttonPublicar.setOnClickListener {
            Publicacao()
        }
    }

    private fun Publicacao() {
        val nomeArquivoGet: TextView = findViewById(R.id.editTNome)
        val nomeArquivo = nomeArquivoGet.text.toString()

        val descArquivoGet: TextView = findViewById(R.id.editTextDescricao)
        val descArquivo = descArquivoGet.text.toString()

        val dados = mapOf(
            "nome" to nomeArquivo,
            "descricao" to descArquivo
        )
        if (uriArquivo != null && nomeArquivo.isNotEmpty() && descArquivo.isNotEmpty()) {
            val storegeRef = storage.reference
            val mountainsRef = storegeRef.child("Biblioteca")
            val mountainFileRef = mountainsRef.child("Livros").child(nomeArquivo)

            var uploadFile = mountainFileRef.putFile(uriArquivo!!)

                uploadFile.addOnSuccessListener { task ->
                    var url = task.metadata?.reference?.downloadUrl
                    bancoDeDados
                        .collection("Biblioteca")
                        .document(url.toString())
                        .set(dados).addOnSuccessListener {
                            startActivity(Intent(this, Home :: class.java))
                        }
                }
        }else{
            binding.alert.text = "Campo Nome/Descrição não preenchido"
        }
    }
}