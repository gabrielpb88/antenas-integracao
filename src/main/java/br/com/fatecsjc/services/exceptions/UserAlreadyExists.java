package br.com.fatecsjc.services.exceptions;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists(){
        super("Email já cadastrado");
    }

    public UserAlreadyExists(String message){
        super(message);
    }
}
