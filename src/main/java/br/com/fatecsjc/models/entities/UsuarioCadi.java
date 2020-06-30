package br.com.fatecsjc.models.entities;

import br.com.fatecsjc.models.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioCadi extends Usuario {

    public UsuarioCadi(){
        this.tipoUsuario = TipoUsuario.CADI;
    }

    private Integer nivel;

}
