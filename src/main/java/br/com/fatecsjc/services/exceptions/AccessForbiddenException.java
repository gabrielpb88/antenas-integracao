package br.com.fatecsjc.services.exceptions;

public class AccessForbiddenException extends RuntimeException {

    public AccessForbiddenException(){
        super("Usuário inexistente ou inativo");
    }

    public AccessForbiddenException(String message){
        super(message);
    }
}
