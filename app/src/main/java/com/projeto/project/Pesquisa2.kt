package com.projeto.project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.projeto.project.databinding.ActivityPesquisa2Binding

class Pesquisa2 : AppCompatActivity() {

    private val binding by lazy { ActivityPesquisa2Binding.inflate(layoutInflater) }
    private val bancoDeDados by lazy { FirebaseFirestore.getInstance() }
    private lateinit var livroAdapter: LivroAdapter
    private val listaLivros = mutableListOf<Livro>()
    private var listaLivrosFiltrados = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        binding.btnPost.setOnClickListener {
            val intent = Intent(this, Post::class.java)
            startActivity(intent)
        }

        inicializandoToolbarPrincipal()
        buscarLivrosFirestore()

        // Listener para o botão de pesquisa
        binding.imageButton.setOnClickListener {
            val query = binding.editTextText2.text.toString().trim()
            if (query.isNotEmpty()) {
                pesquisarLivros(query)
            } else {
                Toast.makeText(this, "Digite um nome para pesquisar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inicializandoToolbarPrincipal() {
        binding.tbPrincipal2.inflateMenu(R.menu.top_bar)
        binding.tbPrincipal2.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuConfig -> {
                    val intent = Intent(this, Configuracoes::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun inicializarRecyclerView() {
        livroAdapter = LivroAdapter(listaLivrosFiltrados, this)
        binding.listaLivros.apply {
            layoutManager = LinearLayoutManager(this@Pesquisa2)
            adapter = livroAdapter
        }
    }

    private fun buscarLivrosFirestore() {
        bancoDeDados.collection("Biblioteca")
            .get()
            .addOnSuccessListener { documents ->
                listaLivros.clear()
                for (document in documents) {
                    val nome = document.getString("nome") ?: "Nome não disponível"
                    val descricao = document.getString("descricao") ?: "Descrição não disponível"
                    val imagemUrl = document.getString("imagemUrl") ?: ""
                    val arquivoUrl = document.getString("arquivoUrl")

                    val livro = Livro(nome, descricao, imagemUrl, arquivoUrl)
                    listaLivros.add(livro)
                }
                listaLivrosFiltrados.addAll(listaLivros) // Inicializa a lista filtrada com todos os livros
                inicializarRecyclerView()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar livros: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun pesquisarLivros(query: String) {
        listaLivrosFiltrados.clear()
        listaLivrosFiltrados.addAll(listaLivros.filter { livro ->
            livro.nome.contains(query, ignoreCase = true) // Pesquisa pelo nome do livro
        })
        livroAdapter.notifyDataSetChanged() // Atualiza o adaptador para refletir a nova lista filtrada
    }
}
