package com.template.model.dao;

import com.template.Conexao;
import com.template.model.dto.LivrosDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivrosDAO {
    private static final Logger logger = Logger.getLogger(LivrosDAO.class.getName());
    private final Conexao conexao = new Conexao();
    public List<LivrosDTO> listarLivros() {
        List<LivrosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (Connection conn = conexao.conectaBD();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                LivrosDTO livro = new LivrosDTO();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setGenero(rs.getString("genero"));
                livro.setPreco(rs.getDouble("preco"));
                lista.add(livro);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar livros", e);
            throw new RuntimeException("Erro ao listar livros.", e);
        }
        return lista;
    }
    public void cadastrarLivro(LivrosDTO livro) {
        String sql = "INSERT INTO livros " + "(titulo, autor, ano_publicacao, genero, preco) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conexao.conectaBD();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, livro.getTitulo());
            pstm.setString(2, livro.getAutor());
            pstm.setInt(3, livro.getAnoPublicacao());
            pstm.setString(4, livro.getGenero());
            pstm.setDouble(5, livro.getPreco());
            pstm.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar livro", e);
            throw new RuntimeException("Erro ao cadastrar livro.", e);
        }
    }
    public void atualizarLivro(LivrosDTO livro) {
        String sql = "UPDATE livros SET " + "titulo = ?, " + "autor = ?, " + "ano_publicacao = ?, " + "genero = ?, " + "preco = ? " + "WHERE id = ?";
        try (Connection conn = conexao.conectaBD();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, livro.getTitulo());
            pstm.setString(2, livro.getAutor());
            pstm.setInt(3, livro.getAnoPublicacao());
            pstm.setString(4, livro.getGenero());
            pstm.setDouble(5, livro.getPreco());
            pstm.setInt(6, livro.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar livro", e
            );
            throw new RuntimeException("Erro ao atualizar livro.", e);
        }
    }
    public void deletarLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection conn = conexao.conectaBD();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao deletar livro", e);
            throw new RuntimeException("Erro ao deletar livro.", e);
        }
    }
}