package com.template.validator;

public class EmailValidator implements Validator<String> {

    private final String valor;
    public EmailValidator(String valor) {
        this.valor = valor;
    }
    @Override
    public boolean validar(String valor) {
        return valor != null && valor.contains("@") && valor.contains(".");
    }
    @Override
    public String getMensagemErro() {
        return "O e-mail informado não é válido.";
    }
    @Override
    public String getValor() {
        return valor;
    }
}