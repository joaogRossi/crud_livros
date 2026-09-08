package com.template.validator;

public class AutorValidator implements Validator<String> {

    private final String autor;
    public AutorValidator(String autor) {
        this.autor = autor;
    }
    @Override
    public boolean validar(String valor) {
        if (autor == null || autor.trim().isEmpty()) {
            return false;
        }
        String autorLimpo = autor.trim();
        if (autorLimpo.length() < 3) {
            return false;
        }
        return autorLimpo.matches("[\\p{L} .'-]+");
    }
    @Override
    public String getMensagemErro() {
        return "Autor inválido. Digite um nome válido.";
    }
    @Override
    public String getValor() {
        return autor;
    }
}
