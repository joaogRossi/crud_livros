package com.template;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivrosDAO {
    private static final Logger logger = Logger.getLogger(LivrosDAO.class.getName());
    static {
        logger.setUseParentHandlers(false);
    }
    public void cadastrarLivro(LivrosDTO livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, genero, preco) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = new Conexao().conectaBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt(3, livro.getAnoPublicacao());
            ps.setString(4, livro.getGenero());
            ps.setDouble(5, livro.getPreco());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar livro.", e);
        }
    }
    public List<LivrosDTO> listarLivros() {
        List<LivrosDTO> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, ano_publicacao, genero, preco FROM livros ORDER BY id";
        try (Connection con = new Conexao().conectaBD();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
            logger.log(Level.SEVERE, "Erro ao listar livros.", e);
        }
        return lista;
    }
    public void atualizarLivro(LivrosDTO livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, ano_publicacao = ?, genero = ?, preco = ? WHERE id = ?";
        try (Connection con = new Conexao().conectaBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setInt(3, livro.getAnoPublicacao());
            ps.setString(4, livro.getGenero());
            ps.setDouble(5, livro.getPreco());
            ps.setInt(6, livro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar livro.", e);
        }
    }
    public void deletarLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection con = new Conexao().conectaBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao deletar livro.", e);
        }
    }
    public LivrosDTO buscarPorId(int id) {
        String sql = "SELECT id, titulo, autor, ano_publicacao, genero, preco FROM livros WHERE id = ?";
        try (Connection con = new Conexao().conectaBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LivrosDTO livro = new LivrosDTO();

                    livro.setId(rs.getInt("id"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                    livro.setGenero(rs.getString("genero"));
                    livro.setPreco(rs.getDouble("preco"));

                    return livro;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar livro por ID.", e);
        }

        return null;
    }
}