package com.template.service;

import com.template.model.dao.LivrosDAO;
import com.template.model.dto.LivrosDTO;
import java.util.ArrayList;
import java.util.List;

public class LivrosService {
    private final LivrosDAO livrosDAO = new LivrosDAO();
    public List<LivrosDTO> listarLivros() {
        return livrosDAO.listarLivros();
    }
    public void cadastrarLivro(LivrosDTO livro) {
        livrosDAO.cadastrarLivro(livro);
    }
    public void atualizarLivro(LivrosDTO livro) {
        livrosDAO.atualizarLivro(livro);
    }
    public void deletarLivro(int id) {
        livrosDAO.deletarLivro(id);
    }
    public List<LivrosDTO> filtrarLivros(
            List<LivrosDTO> livros,
            String pesquisa) {
        if (pesquisa == null || pesquisa.isBlank()) {
            return livros;
        }
        List<LivrosDTO> listaFiltrada = new ArrayList<>();
        for (LivrosDTO livro : livros) {
            String id = String.valueOf(livro.getId());
            String titulo = livro.getTitulo()
                    .toLowerCase();
            String autor = livro.getAutor()
                    .toLowerCase();
            String genero = livro.getGenero()
                    .toLowerCase();
            if (id.contains(pesquisa)
                    || titulo.contains(pesquisa)
                    || autor.contains(pesquisa)
                    || genero.contains(pesquisa)) {
                listaFiltrada.add(livro);
            }
        }
        return listaFiltrada;
    }
}