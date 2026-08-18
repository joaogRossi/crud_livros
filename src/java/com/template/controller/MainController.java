package com.template.controller;

import com.template.model.dto.LivrosDTO;
import com.template.service.LivrosService;
import com.template.util.DialogUtil;
import com.template.validator.LivrosValidator;
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

    private final LivrosService livrosService = new LivrosService();
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
        spnAnoPublicacao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2030, 2024));
        spnAnoPublicacao.setEditable(false);
        cbGenero.setItems(
                FXCollections.observableArrayList(
                        "Romance",
                        "Aventura",
                        "Fantasia",
                        "Terror",
                        "Suspense",
                        "Biografia",
                        "Didático",
                        "Outro"
                )
        );
        txtTitulo.setPromptText("Digite o título do livro");
        txtAutor.setPromptText("Digite o nome do autor");
        txtPreco.setPromptText("Ex: 49.90");
        txtPesquisa.setPromptText("Pesquisar por título, autor, gênero ou ID");
        txtPreco.setTextFormatter(
                new TextFormatter<>(change -> {
                    String texto = change.getControlNewText();
                    if (texto.matches("\\d*([.]\\d{0,2})?")) {return change;}
                    return null;
                })
        );
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
        listaLivros = FXCollections.observableArrayList(livrosService.listarLivros());
        tblLivros.setItems(listaLivros);
        atualizarContador(listaLivros.size());
    }
    private void filtrarLivros() {
        String pesquisa = txtPesquisa.getText().toLowerCase().trim();
        ObservableList<LivrosDTO> listaFiltrada = FXCollections.observableArrayList(
                        livrosService.filtrarLivros(
                                listaLivros,
                                pesquisa
                        )
                );
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
        }
    }
    @FXML
    private void btnCadastrarAction() {
        if (!validarCampos()) {
            return;
        }
        LivrosDTO livro = criarLivro();
        livrosService.cadastrarLivro(livro);
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
        if (!validarCampos()) {return;}
        livroSelecionado.setTitulo(txtTitulo.getText().trim());
        livroSelecionado.setAutor(txtAutor.getText().trim());
        livroSelecionado.setAnoPublicacao(spnAnoPublicacao.getValue());
        livroSelecionado.setGenero(cbGenero.getValue());
        livroSelecionado.setPreco(Double.parseDouble(txtPreco.getText().trim()));
        livrosService.atualizarLivro(livroSelecionado);
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
        livrosService.deletarLivro(livroSelecionado.getId());
        carregarLivros();
        limparCampos();
        DialogUtil.showInfo("Livro excluído com sucesso!");
    }
    @FXML
    private void btnLimparAction() {
        limparCampos();
    }
    private boolean validarCampos() {
        String erro = LivrosValidator.validar(
                txtTitulo.getText(),
                txtAutor.getText(),
                cbGenero.getValue(),
                txtPreco.getText()
        );
        if (erro != null) {
            DialogUtil.showError(erro);
            return false;
        }
        return true;
    }
    private void verificarCampos() {
        boolean camposPreenchidos =
                LivrosValidator.camposPreenchidos(
                        txtTitulo.getText(),
                        txtAutor.getText(),
                        cbGenero.getValue(),
                        txtPreco.getText()
                );
        boolean temLivroSelecionado = tblLivros.getSelectionModel().getSelectedItem() != null;
        btnCadastrar.setDisable(!camposPreenchidos || temLivroSelecionado
        );
    }
    private LivrosDTO criarLivro() {
        LivrosDTO livro = new LivrosDTO();
        livro.setTitulo(txtTitulo.getText().trim());
        livro.setAutor(txtAutor.getText().trim());
        livro.setAnoPublicacao(spnAnoPublicacao.getValue());
        livro.setGenero(cbGenero.getValue());
        livro.setPreco(Double.parseDouble(txtPreco.getText().trim()));
        return livro;
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