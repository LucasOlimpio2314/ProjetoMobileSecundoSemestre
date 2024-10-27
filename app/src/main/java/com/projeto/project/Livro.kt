package com.projeto.project

data class Livro(
    val nome: String,
    val descricao: String,
    val imagemUrl: String, // Adiciona o campo da URL da imagem
    val arquivoUrl: String?
)