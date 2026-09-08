package com.template.validator;

public class PrecoValidator implements Validator<String> {

    private final String valor;

    public PrecoValidator(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean validar(String valorAtual) {
        if (this.valor == null) {
            return false;
        }
        try {
            return Double.parseDouble(this.valor.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @Override
    public String getMensagemErro() {
        return "Preço inválido. Ex: 49.90";
    }
    @Override
    public String getValor() {
        return valor;
    }
}
