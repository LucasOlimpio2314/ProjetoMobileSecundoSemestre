package com.projeto.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.projeto.project.databinding.ActivityPostBinding

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
    private var uriImagem: Uri? = null // Nova variável para a imagem do livro

    private val pegarPdf = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.textNomedoArq.text = uri.toString()
            uriArquivo = uri
        } else {
            Toast.makeText(this, "Nenhum arquivo selecionado", Toast.LENGTH_LONG).show()
        }
    }

    private val pegarImagem = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.imagemLivro3.setImageURI(uri) // Exibe a imagem na ImageView
            uriImagem = uri
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_LONG).show()
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
            pegarPdf.launch("application/*")
        }

        binding.btnSelecionarImagem.setOnClickListener {
            pegarImagem.launch("image/*")
        }

        binding.buttonPublicar.setOnClickListener {
            binding.buttonPublicar.isEnabled = false // Desativa o botão ao iniciar
            verificarNomeExistente() // Verifica se o nome do arquivo já existe
        }
    }

    private fun verificarNomeExistente() {
        val nomeArquivoGet: TextView = findViewById(R.id.editTNome)
        val nomeArquivo = nomeArquivoGet.text.toString()

        if (nomeArquivo.isEmpty()) {
            Toast.makeText(this, "O nome do arquivo não pode estar vazio.", Toast.LENGTH_LONG).show()
            binding.buttonPublicar.isEnabled = true // Reativa o botão caso o nome esteja vazio
            return
        }

        bancoDeDados.collection("Biblioteca")
            .whereEqualTo("nome", nomeArquivo)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Toast.makeText(this, "Esse nome de arquivo já existe. Escolha outro nome.", Toast.LENGTH_LONG).show()
                    binding.buttonPublicar.isEnabled = true // Reativa o botão caso o nome já exista
                } else {
                    Publicacao(nomeArquivo) // Continua com a publicação se o nome não existir
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar o nome: ${e.message}", Toast.LENGTH_LONG).show()
                binding.buttonPublicar.isEnabled = true // Reativa o botão caso ocorra erro
            }
    }

    private fun Publicacao(nomeArquivo: String) {
        val descArquivoGet: TextView = findViewById(R.id.editTextDescricao)
        val descArquivo = descArquivoGet.text.toString()

        val livroId = bancoDeDados.collection("Biblioteca").document().id

        val dados = mapOf(
            "id" to livroId,
            "nome" to nomeArquivo,
            "descricao" to descArquivo,
            "imagemUrl" to "",
            "arquivoUrl" to ""
        )

        if (uriArquivo != null && uriImagem != null && nomeArquivo.isNotEmpty() && descArquivo.isNotEmpty()) {
            val storageRef = storage.reference
            val mountainsRef = storageRef.child("Biblioteca/Livros/$livroId")

            val uploadFile = mountainsRef.child("documento").putFile(uriArquivo!!)

            uploadFile.addOnSuccessListener {
                mountainsRef.child("documento").downloadUrl.addOnSuccessListener { fileUrl ->
                    val uploadImage = mountainsRef.child("imagem").putFile(uriImagem!!)
                    uploadImage.addOnSuccessListener {
                        mountainsRef.child("imagem").downloadUrl.addOnSuccessListener { imageUrl ->
                            bancoDeDados.collection("Biblioteca")
                                .document(livroId)
                                .set(dados + mapOf(
                                    "imagemUrl" to imageUrl.toString(),
                                    "arquivoUrl" to fileUrl.toString()
                                ))
                                .addOnSuccessListener {
                                    startActivity(Intent(this, Home::class.java))
                                    finish()
                                }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_LONG).show()
                        binding.buttonPublicar.isEnabled = true // Reativa o botão em caso de erro
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao fazer upload do PDF: ${e.message}", Toast.LENGTH_LONG).show()
                    binding.buttonPublicar.isEnabled = true // Reativa o botão em caso de erro
                }
            }
        } else {
            binding.alert.text = "Campo Nome/Descrição não preenchido ou imagem/arquivo não selecionado."
            binding.buttonPublicar.isEnabled = true // Reativa o botão se campos estiverem incompletos
        }
    }
}
