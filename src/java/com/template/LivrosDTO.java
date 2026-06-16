package com.template;

public class LivrosDTO {

    private int id;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private String genero;
    private double preco;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public int getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
}

/* 3 melhorias de Layout:
Emoji de livros
Formulário separado em um painel/card branco, com espaçamento melhor.
Tabela mais bonita, com cabeçalho colorido, colunas ajustadas e linha selecionada destacada.

3 melhorias de Usabilidade:

Spinner para ano;
ComboBox para gênero;
campo de pesquisa;*/