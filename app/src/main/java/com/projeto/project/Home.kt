package com.projeto.project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.projeto.project.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val bancoDeDados by lazy { FirebaseFirestore.getInstance() }
    private lateinit var livroAdapter: LivroAdapter2
    private val listaLivros = mutableListOf<Livro>()
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
        inicializarRecyclerView()
        buscarLivrosFirestore()
        binding.btnPost.setOnClickListener {
            val intent = Intent(this,Post ::class.java)
            startActivity(intent)
        }
        binding.btnBusca.setOnClickListener {
            val intent = Intent(this,Pesquisa2 ::class.java)
            startActivity(intent)
        }

    }

    private fun inicializandoToolbarPrincipal() {
        binding.tbPrincipal.inflateMenu(R.menu.top_bar)
        binding.tbPrincipal.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menuConfig -> {
                    val intent = Intent(this,Configuracoes ::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener true
                }
            }
        }
    }

    private fun inicializarRecyclerView() {
        // Configuração do RecyclerView
        livroAdapter = LivroAdapter2(listaLivros, this) // Ajustado para LivroAdapter2
        binding.listaLivrosHome.apply {
            layoutManager = LinearLayoutManager(this@Home)
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
                    val imagemUrl = document.getString("imagemUrl") ?: "" // Pega a URL da imagem
                    val arquivoUrl = document.getString("arquivoUrl") // Pega a URL do arquivo

                    // Cria o objeto Livro com todas as informações
                    val livro = Livro(nome, descricao, imagemUrl, arquivoUrl)
                    listaLivros.add(livro)
                }
                livroAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar livros: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }


}