package com.template;

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
    @FXML private TableColumn<LivrosDTO, Integer> colAno_publicacao;
    @FXML private TableColumn<LivrosDTO, String> colGenero;
    @FXML private TableColumn<LivrosDTO, Double> colPreco;
    @FXML private TextField txtPesquisa;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private Spinner<Integer> spnAno_publicacao;
    @FXML private ComboBox<String> cbGenero;
    @FXML private TextField txtPreco;
    @FXML private Button btnCadastrar;
    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;
    @FXML private Label lblMensagem;
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
        colAno_publicacao.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tblLivros.setOnMouseClicked(event -> carregarCampos());
    }
    private void configurarCampos() {
        spnAno_publicacao.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2030, 2024)
        );
        spnAno_publicacao.setEditable(false);
        cbGenero.setItems(FXCollections.observableArrayList(
                "Romance",
                "Aventura",
                "Fantasia",
                "Terror",
                "Suspense",
                "Biografia",
                "Didático",
                "Outro"
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
            if (id.contains(pesquisa)
                    || titulo.contains(pesquisa)
                    || autor.contains(pesquisa)
                    || genero.contains(pesquisa)) {
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
            spnAno_publicacao.getValueFactory().setValue(livro.getAnoPublicacao());
            cbGenero.setValue(livro.getGenero());
            txtPreco.setText(String.valueOf(livro.getPreco()));
            btnEditar.setDisable(false);
            btnDeletar.setDisable(false);
            btnCadastrar.setDisable(true);
            mostrarMensagem("Livro selecionado para edição ou exclusão.", "aviso");
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
        livro.setAnoPublicacao(spnAno_publicacao.getValue());
        livro.setGenero(cbGenero.getValue());
        livro.setPreco(Double.parseDouble(txtPreco.getText()));
        livrosDAO.cadastrarLivro(livro);
        carregarLivros();
        limparCampos();
        mostrarMensagem("Livro cadastrado com sucesso!", "sucesso");
    }
    @FXML
    private void btnEditarAction() {
        LivrosDTO livroSelecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (livroSelecionado == null) {
            mostrarMensagem("Selecione um livro para editar.", "erro");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        livroSelecionado.setTitulo(txtTitulo.getText().trim());
        livroSelecionado.setAutor(txtAutor.getText().trim());
        livroSelecionado.setAnoPublicacao(spnAno_publicacao.getValue());
        livroSelecionado.setGenero(cbGenero.getValue());
        livroSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
        livrosDAO.atualizarLivro(livroSelecionado);
        carregarLivros();
        limparCampos();
        mostrarMensagem("Livro editado com sucesso!", "sucesso");
    }
    @FXML
    private void btnDeletarAction() {
        LivrosDTO livroSelecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (livroSelecionado == null) {
            mostrarMensagem("Selecione um livro para excluir.", "erro");
            return;
        }
        livrosDAO.deletarLivro(livroSelecionado.getId());
        carregarLivros();
        limparCampos();
        mostrarMensagem("Livro excluído com sucesso!", "sucesso");
    }
    @FXML
    private void btnLimparAction() {
        limparCampos();
        mostrarMensagem("Campos limpos.", "aviso");
    }
    private boolean validarCampos() {
        limparEstilosErro();
        if (txtTitulo.getText().trim().isEmpty()) {
            txtTitulo.getStyleClass().add("campo-erro");
            mostrarMensagem("Informe o título do livro.", "erro");
            return false;
        }
        if (txtAutor.getText().trim().isEmpty()) {
            txtAutor.getStyleClass().add("campo-erro");
            mostrarMensagem("Informe o autor do livro.", "erro");
            return false;
        }
        if (cbGenero.getValue() == null) {
            cbGenero.getStyleClass().add("campo-erro");
            mostrarMensagem("Selecione o gênero do livro.", "erro");
            return false;
        }
        if (txtPreco.getText().trim().isEmpty()) {
            txtPreco.getStyleClass().add("campo-erro");
            mostrarMensagem("Informe o preço do livro.", "erro");
            return false;
        }
        try {
            Double.parseDouble(txtPreco.getText());
        } catch (NumberFormatException e) {
            txtPreco.getStyleClass().add("campo-erro");
            mostrarMensagem("Preço inválido. Use ponto para casas decimais. Ex: 49.90", "erro");
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
        spnAno_publicacao.getValueFactory().setValue(2024);
        cbGenero.setValue(null);
        txtPreco.clear();
        tblLivros.getSelectionModel().clearSelection();
        btnCadastrar.setDisable(true);
        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
        limparEstilosErro();
        txtTitulo.requestFocus();
    }
    private void limparEstilosErro() {
        txtTitulo.getStyleClass().remove("campo-erro");
        txtAutor.getStyleClass().remove("campo-erro");
        cbGenero.getStyleClass().remove("campo-erro");
        txtPreco.getStyleClass().remove("campo-erro");
    }
    private void mostrarMensagem(String mensagem, String tipo) {
        lblMensagem.setText(mensagem);

        lblMensagem.getStyleClass().removeAll(
                "mensagem-sucesso",
                "mensagem-erro",
                "mensagem-aviso"
        );
        if (tipo.equals("sucesso")) {
            lblMensagem.getStyleClass().add("mensagem-sucesso");
        } else if (tipo.equals("erro")) {
            lblMensagem.getStyleClass().add("mensagem-erro");
        } else {
            lblMensagem.getStyleClass().add("mensagem-aviso");
        }
    }
}