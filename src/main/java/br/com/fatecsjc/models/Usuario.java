package br.com.fatecsjc.models;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class Usuario {

    protected ObjectId id;
    protected TipoUsuario tipoUsuario;
    protected String nome;
    protected String email;
    protected String senha;

}
