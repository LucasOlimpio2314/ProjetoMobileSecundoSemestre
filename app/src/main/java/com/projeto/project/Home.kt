package com.projeto.project

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.projeto.project.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
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

}