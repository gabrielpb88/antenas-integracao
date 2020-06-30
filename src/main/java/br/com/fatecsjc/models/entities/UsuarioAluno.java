package br.com.fatecsjc.models.entities;

import br.com.fatecsjc.models.TipoUsuario;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class UsuarioAluno extends Usuario {

    public UsuarioAluno(){
        this.tipoUsuario = TipoUsuario.ALUNO;
    }

    private List<MedalhaAtribuida> medalhas = new ArrayList<>();

}
