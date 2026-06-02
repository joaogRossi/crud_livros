package com.template;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML private TableView<LivrosDTO> tblLivros;

    @FXML private TableColumn<LivrosDTO, Integer> colID;
    @FXML private TableColumn<LivrosDTO, String> colTitulo;
    @FXML private TableColumn<LivrosDTO, String> colAutor;
    @FXML private TableColumn<LivrosDTO, Integer> colAno_publicacao;
    @FXML private TableColumn<LivrosDTO, String> colGenero;
    @FXML private TableColumn<LivrosDTO, Double> colPreco;

    @FXML private TextField txtID;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAno_publicacao;
    @FXML private TextField txtGenero;
    @FXML private TextField txtPreco;

    private final LivrosDAO livrosDAO = new LivrosDAO();

    @FXML
    private void initialize() {
        configurarTabela();
        carregarLivros();
    }

    private void configurarTabela() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAno_publicacao.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }

    private void carregarLivros() {
        tblLivros.setItems(FXCollections.observableArrayList(livrosDAO.listarLivros()));
    }

    @FXML
    private void carregarCampos() {
        LivrosDTO livro = tblLivros.getSelectionModel().getSelectedItem();

        if (livro != null) {
            txtID.setText(String.valueOf(livro.getId()));
            txtTitulo.setText(livro.getTitulo());
            txtAutor.setText(livro.getAutor());
            txtAno_publicacao.setText(String.valueOf(livro.getAnoPublicacao()));
            txtGenero.setText(livro.getGenero());
            txtPreco.setText(String.valueOf(livro.getPreco()));
        }
    }

    @FXML
    private void btnCadastrarAction() {
        try {
            LivrosDTO livro = new LivrosDTO();

            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setAnoPublicacao(Integer.parseInt(txtAno_publicacao.getText()));
            livro.setGenero(txtGenero.getText());
            livro.setPreco(Double.parseDouble(txtPreco.getText()));

            livrosDAO.cadastrarLivro(livro);

            carregarLivros();
            limparCampos();

            mostrarAlerta("Livro cadastrado com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Ano e preço precisam ser números.");
        }
    }

    @FXML
    private void btnEditarAction() {
        try {
            if (txtID.getText().isEmpty()) {
                mostrarAlerta("Selecione um livro para editar.");
                return;
            }

            LivrosDTO livro = new LivrosDTO();

            livro.setId(Integer.parseInt(txtID.getText()));
            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setAnoPublicacao(Integer.parseInt(txtAno_publicacao.getText()));
            livro.setGenero(txtGenero.getText());
            livro.setPreco(Double.parseDouble(txtPreco.getText()));

            livrosDAO.atualizarLivro(livro);

            carregarLivros();
            limparCampos();

            mostrarAlerta("Livro editado com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Ano e preço precisam ser números.");
        }
    }

    @FXML
    private void btnDeletarAction() {
        try {
            if (txtID.getText().isEmpty()) {
                mostrarAlerta("Selecione um livro para deletar.");
                return;
            }

            int id = Integer.parseInt(txtID.getText());

            livrosDAO.deletarLivro(id);

            carregarLivros();
            limparCampos();

            mostrarAlerta("Livro deletado com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("ID inválido.");
        }
    }

    @FXML
    private void btnLimparAction() {
        limparCampos();
    }

    private void limparCampos() {
        txtID.clear();
        txtTitulo.clear();
        txtAutor.clear();
        txtAno_publicacao.clear();
        txtGenero.clear();
        txtPreco.clear();
        tblLivros.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}