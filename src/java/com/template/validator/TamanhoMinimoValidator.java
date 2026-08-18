package com.template.validator;

public class TamanhoMinimoValidator implements Validator<String> {

    private final String valor;
    private final int tamanhoMinimo;
    public TamanhoMinimoValidator(String valor, int tamanhoMinimo) {
        this.valor = valor;
        this.tamanhoMinimo = tamanhoMinimo;
    }
    @Override
    public boolean validar(String valor) {
        return valor != null && valor.length() >= tamanhoMinimo;
    }
    @Override
    public String getMensagemErro() {
        return "O campo deve possuir pelo menos "
                + tamanhoMinimo + " caracteres.";
    }
    @Override
    public String getValor() {
        return valor;
    }
}