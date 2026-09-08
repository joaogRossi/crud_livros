package com.template.validator;

public class CampoObrigatorioValidator implements Validator<String> {
    private final String nomeCampo;
    private final String valor;

    public CampoObrigatorioValidator(String nomeCampo, String valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }
    @Override
    public boolean validar(String valorAtual) { // o valor do parâmetro é o que será validado neste ciclo
        return this.valor != null && !this.valor.trim().isEmpty();
    }
    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " deve ser preenchido.";
    }
    @Override
    public String getValor() {
        return valor;
    }
}
