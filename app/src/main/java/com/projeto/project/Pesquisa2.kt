package com.projeto.project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.projeto.project.databinding.ActivityHomeBinding
import com.projeto.project.databinding.ActivityPesquisa2Binding

class Pesquisa2 : AppCompatActivity() {

    private val binding by lazy {
        ActivityPesquisa2Binding.inflate(layoutInflater)
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
        binding.btnPost.setOnClickListener {
            val intent = Intent(this,Post ::class.java)
            startActivity(intent)
        }
        binding.btnHome.setOnClickListener {
            finish()
        }
    }

    private fun inicializandoToolbarPrincipal() {
        binding.tbPrincipal2.inflateMenu(R.menu.top_bar)
        binding.tbPrincipal2.setOnMenuItemClickListener { menuItem ->
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
}