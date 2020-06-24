package br.com.fatecsjc.models.entities;

import br.com.fatecsjc.models.TipoUsuario;

public class UsuarioProfessor extends Usuario {

    public UsuarioProfessor(){
        this.setTipoUsuario(TipoUsuario.PROFESSOR);
    }
}
