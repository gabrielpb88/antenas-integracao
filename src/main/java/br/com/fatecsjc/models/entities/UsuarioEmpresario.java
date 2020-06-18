package br.com.fatecsjc.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(callSuper=false)
public class UsuarioEmpresario extends Usuario {

    private String empresa;
    private String cpf;

}
