package br.com.fatecsjc.models;

public enum TipoUsuario {

    ALUNO(1),
    PROFESSOR(2),
    CADI(3),
    EMPRESARIO(4);

    private Integer tipo;

    TipoUsuario(Integer tipo){
        this.tipo = tipo;
    }
}
