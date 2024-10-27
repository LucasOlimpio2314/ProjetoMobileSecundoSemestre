package com.projeto.project

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log

class LivroAdapter2(private var listaLivros: List<Livro>, private val context: Context) :
    RecyclerView.Adapter<LivroAdapter2.LivroViewHolder>() { // Corrigido para LivroAdapter2

    inner class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomeLivro: TextView = itemView.findViewById(R.id.tvNomeLivro)
        val tvDescricaoLivro: TextView = itemView.findViewById(R.id.tvDescricaoLivro)
        val ivImagemLivro: ImageView = itemView.findViewById(R.id.ivImagemLivro)
        val btnBaixar: Button = itemView.findViewById(R.id.BaixarLivro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_livro_2, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = listaLivros[position]
        holder.tvNomeLivro.text = livro.nome
        holder.tvDescricaoLivro.text = livro.descricao

        // Carregar a imagem com Glide
        Glide.with(holder.itemView.context)
            .load(livro.imagemUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(holder.ivImagemLivro)

        holder.btnBaixar.setOnClickListener {
            if (livro.arquivoUrl != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(livro.arquivoUrl, context)
                } else {
                    Toast.makeText(context, "Permissão de armazenamento não concedida.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(holder.itemView.context, "Arquivo não disponível para download.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = listaLivros.size

    fun updateList(novaLista: List<Livro>) {
        listaLivros = novaLista
        notifyDataSetChanged()
    }

    private fun downloadFile(fileUrl: String?, context: Context) {
        if (fileUrl != null) {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl)
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val localFile = File(downloadsDir, "livro_${System.currentTimeMillis()}.pdf")

            storageRef.getFile(localFile)
                .addOnSuccessListener {
                    Toast.makeText(context, "Download concluído: ${localFile.absolutePath}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Erro ao baixar arquivo: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("DownloadError", "Erro ao baixar arquivo", exception)
                }
        } else {
            Toast.makeText(context, "URL do arquivo inválida", Toast.LENGTH_SHORT).show()
        }
    }
}
