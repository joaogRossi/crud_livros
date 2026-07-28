package com.template.controller;

import com.template.model.dao.LivrosDAO;
import com.template.model.dto.LivrosDTO;
import com.template.util.DialogUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML private TableView<LivrosDTO> tblLivros;
    @FXML private TableColumn<LivrosDTO, Integer> colID;
    @FXML private TableColumn<LivrosDTO, String> colTitulo;
    @FXML private TableColumn<LivrosDTO, String> colAutor;
    @FXML private TableColumn<LivrosDTO, Integer> colAnoPublicacao;
    @FXML private TableColumn<LivrosDTO, String> colGenero;
    @FXML private TableColumn<LivrosDTO, Double> colPreco;

    @FXML private TextField txtPesquisa;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private Spinner<Integer> spnAnoPublicacao;
    @FXML private ComboBox<String> cbGenero;
    @FXML private TextField txtPreco;

    @FXML private Button btnCadastrar;
    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;

    @FXML private Label lblContador;

    private final LivrosDAO livrosDAO = new LivrosDAO();
    private ObservableList<LivrosDTO> listaLivros = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarTabela();
        configurarCampos();
        configurarBotoes();
        carregarLivros();
    }

    private void configurarTabela() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnoPublicacao.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        tblLivros.setOnMouseClicked(event -> carregarCampos());
    }

    private void configurarCampos() {
        spnAnoPublicacao.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2030, 2024)
        );
        spnAnoPublicacao.setEditable(false);

        cbGenero.setItems(FXCollections.observableArrayList(
                "Romance", "Aventura", "Fantasia", "Terror",
                "Suspense", "Biografia", "Didático", "Outro"
        ));

        txtTitulo.setPromptText("Digite o título do livro");
        txtAutor.setPromptText("Digite o nome do autor");
        txtPreco.setPromptText("Ex: 49.90");
        txtPesquisa.setPromptText("Pesquisar por título, autor, gênero ou ID");

        txtPreco.setTextFormatter(new TextFormatter<>(change -> {
            String texto = change.getControlNewText();
            if (texto.matches("\\d*([.]\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> filtrarLivros());
        txtTitulo.textProperty().addListener((obs, antigo, novo) -> verificarCampos());
        txtAutor.textProperty().addListener((obs, antigo, novo) -> verificarCampos());
        txtPreco.textProperty().addListener((obs, antigo, novo) -> verificarCampos());
        cbGenero.valueProperty().addListener((obs, antigo, novo) -> verificarCampos());
    }

    private void configurarBotoes() {
        btnCadastrar.setDisable(true);
        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private void carregarLivros() {
        listaLivros = FXCollections.observableArrayList(livrosDAO.listarLivros());
        tblLivros.setItems(listaLivros);
        atualizarContador(listaLivros.size());
    }

    private void filtrarLivros() {
        String pesquisa = txtPesquisa.getText().toLowerCase().trim();
        if (pesquisa.isEmpty()) {
            tblLivros.setItems(listaLivros);
            atualizarContador(listaLivros.size());
            return;
        }

        ObservableList<LivrosDTO> listaFiltrada = FXCollections.observableArrayList();
        for (LivrosDTO livro : listaLivros) {
            String id = String.valueOf(livro.getId());
            String titulo = livro.getTitulo().toLowerCase();
            String autor = livro.getAutor().toLowerCase();
            String genero = livro.getGenero().toLowerCase();

            if (id.contains(pesquisa) || titulo.contains(pesquisa) ||
                    autor.contains(pesquisa) || genero.contains(pesquisa)) {
                listaFiltrada.add(livro);
            }
        }
        tblLivros.setItems(listaFiltrada);
        atualizarContador(listaFiltrada.size());
    }

    private void atualizarContador(int quantidade) {
        lblContador.setText("Livros cadastrados: " + quantidade);
    }

    @FXML
    private void carregarCampos() {
        LivrosDTO livro = tblLivros.getSelectionModel().getSelectedItem();
        if (livro != null) {
            txtTitulo.setText(livro.getTitulo());
            txtAutor.setText(livro.getAutor());
            spnAnoPublicacao.getValueFactory().setValue(livro.getAnoPublicacao());
            cbGenero.setValue(livro.getGenero());
            txtPreco.setText(String.valueOf(livro.getPreco()));

            btnEditar.setDisable(false);
            btnDeletar.setDisable(false);
            btnCadastrar.setDisable(true);
            DialogUtil.showWarning("Livro selecionado para edição ou exclusão.");
        }
    }

    @FXML
    private void btnCadastrarAction() {
        if (!validarCampos()) {
            return;
        }

        LivrosDTO livro = new LivrosDTO();
        livro.setTitulo(txtTitulo.getText().trim());
        livro.setAutor(txtAutor.getText().trim());
        livro.setAnoPublicacao(spnAnoPublicacao.getValue());
        livro.setGenero(cbGenero.getValue());
        livro.setPreco(Double.parseDouble(txtPreco.getText()));

        livrosDAO.cadastrarLivro(livro);
        carregarLivros();
        limparCampos();
        DialogUtil.showInfo("Livro cadastrado com sucesso!");
    }

    @FXML
    private void btnEditarAction() {
        LivrosDTO livroSelecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (livroSelecionado == null) {
            DialogUtil.showError("Selecione um livro para editar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        livroSelecionado.setTitulo(txtTitulo.getText().trim());
        livroSelecionado.setAutor(txtAutor.getText().trim());
        livroSelecionado.setAnoPublicacao(spnAnoPublicacao.getValue());
        livroSelecionado.setGenero(cbGenero.getValue());
        livroSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));

        livrosDAO.atualizarLivro(livroSelecionado);
        carregarLivros();
        limparCampos();
        DialogUtil.showInfo("Livro editado com sucesso!");
    }

    @FXML
    private void btnDeletarAction() {
        LivrosDTO livroSelecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (livroSelecionado == null) {
            DialogUtil.showError("Selecione um livro para excluir.");
            return;
        }

        livrosDAO.deletarLivro(livroSelecionado.getId());
        carregarLivros();
        limparCampos();
        DialogUtil.showInfo("Livro excluído com sucesso!");
    }

    @FXML
    private void btnLimparAction() {
        limparCampos();
        DialogUtil.showWarning("Campos limpos.");
    }

    private boolean validarCampos() {
        if (txtTitulo.getText().trim().isEmpty()) {
            DialogUtil.showError("Informe o título do livro.");
            return false;
        }
        if (txtAutor.getText().trim().isEmpty()) {
            DialogUtil.showError("Informe o autor do livro.");
            return false;
        }
        if (cbGenero.getValue() == null) {
            DialogUtil.showError("Selecione o gênero do livro.");
            return false;
        }
        if (txtPreco.getText().trim().isEmpty()) {
            DialogUtil.showError("Informe o preço do livro.");
            return false;
        }
        try {
            Double.parseDouble(txtPreco.getText());
        } catch (NumberFormatException e) {
            DialogUtil.showError("Preço inválido. Ex: 49.90");
            return false;
        }
        return true;
    }

    private void verificarCampos() {
        boolean camposPreenchidos = !txtTitulo.getText().trim().isEmpty()
                && !txtAutor.getText().trim().isEmpty()
                && cbGenero.getValue() != null
                && !txtPreco.getText().trim().isEmpty();

        boolean temLivroSelecionado = tblLivros.getSelectionModel().getSelectedItem() != null;
        btnCadastrar.setDisable(!camposPreenchidos || temLivroSelecionado);
    }

    private void limparCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        spnAnoPublicacao.getValueFactory().setValue(2024);
        cbGenero.setValue(null);
        txtPreco.clear();

        tblLivros.getSelectionModel().clearSelection();
        btnCadastrar.setDisable(true);
        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
        txtTitulo.requestFocus();
    }
}