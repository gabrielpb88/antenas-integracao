package br.com.fatecsjc.models.entities;

import br.com.fatecsjc.models.TipoUsuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Data
public class UsuarioAluno extends Usuario {

    public UsuarioAluno(){
        this.tipoUsuario = TipoUsuario.ALUNO;
    }

    private List<MedalhasAtribuidas> medalhas = new ArrayList<>();

}
