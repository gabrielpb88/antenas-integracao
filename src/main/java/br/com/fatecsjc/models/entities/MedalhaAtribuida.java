package br.com.fatecsjc.models.entities;

import lombok.Data;

import java.util.Date;

@Data
public class MedalhaAtribuida {

    private Medalha medalha;
    private String professor;
    private Date dataAtribuicao = new Date();

}
