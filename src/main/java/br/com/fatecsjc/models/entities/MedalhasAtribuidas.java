package br.com.fatecsjc.models.entities;

import lombok.Data;

import java.util.Date;

@Data
public class MedalhasAtribuidas {

    private Medalha medalha;
    private UsuarioProfessor professor;
    private Date dataAtribuicao = new Date();

}
