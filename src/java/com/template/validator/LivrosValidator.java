package com.template.validator;

import java.util.ArrayList;
import java.util.List;

public class LivrosValidator implements ILivrosValidator {

    @Override
    public boolean camposPreenchidos(String titulo, String autor, String genero, String preco) {
        List<Validator<String>> validadores = new ArrayList<>();
        validadores.add(new CampoObrigatorioValidator("título", titulo));
        validadores.add(new CampoObrigatorioValidator("autor", autor));
        validadores.add(new CampoObrigatorioValidator("gênero", genero));
        validadores.add(new CampoObrigatorioValidator("preço", preco));

        for (Validator<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String validar(String titulo, String autor, String genero, String preco) {
        List<Validator<String>> validadores = new ArrayList<>();
        validadores.add(new CampoObrigatorioValidator("título", titulo));
        validadores.add(new CampoObrigatorioValidator("autor", autor));
        validadores.add(new CampoObrigatorioValidator("gênero", genero));
        validadores.add(new CampoObrigatorioValidator("preço", preco));
        validadores.add(new AutorValidator(autor));
        validadores.add(new PrecoValidator(preco));
        for (Validator<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                return validador.getMensagemErro();
            }
        }
        return null;
    }
}
