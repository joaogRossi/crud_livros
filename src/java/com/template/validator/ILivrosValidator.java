package com.template.validator;

public interface ILivrosValidator {
    boolean camposPreenchidos(String titulo, String autor, String genero, String preco);
    String validar(String titulo, String autor, String genero, String preco);
}
