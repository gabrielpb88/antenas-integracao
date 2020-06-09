package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.AlunoRepository;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class AlunoService {

    private AlunoRepository alunoRepository;

    public AlunoService() {
        alunoRepository = new AlunoRepository();
    }

    public void save(Document aluno) {
        alunoRepository.save(aluno);
    }

    public Document login(String email, String senha) {
        return alunoRepository.login(email, senha);
    }

    public Document update(Document aluno) {
        alunoRepository.update(aluno);
        return alunoRepository.findByEmail(aluno.get("email").toString());
    }

    public FindIterable<Document> findAll() {
        return alunoRepository.findAll();
    }

    public Document findByEmail(String email){
        return alunoRepository.findByEmail(email);
    }

}
