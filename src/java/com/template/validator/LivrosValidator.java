package com.template.validator;

public class LivrosValidator {

    public static boolean camposPreenchidos(
            String titulo,
            String autor,
            String genero,
            String preco) {
        return textoPreenchido(titulo)
                && textoPreenchido(autor)
                && textoPreenchido(genero)
                && textoPreenchido(preco);
    }
    public static String validar(
            String titulo,
            String autor,
            String genero,
            String preco) {
        if (!textoPreenchido(titulo)) {return "Informe o título do livro.";}
        if (!textoPreenchido(autor)) {return "Informe o autor do livro.";}
        if (!textoPreenchido(genero)) {return "Selecione o gênero do livro.";}
        if (!textoPreenchido(preco)) {return "Informe o preço do livro.";}
        if (!precoValido(preco)) {return "Preço inválido. Ex: 49.90";}
        return null;
    }
    private static boolean textoPreenchido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    private static boolean precoValido(String preco) {
        try {
            double valor = Double.parseDouble(preco.trim());
            return valor >= 0;
        } catch (NumberFormatException e) {return false;}
    }
}