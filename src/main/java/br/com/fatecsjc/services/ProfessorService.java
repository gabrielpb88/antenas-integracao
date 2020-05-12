package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.ProfessorRepository;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class ProfessorService {

    private ProfessorRepository professorRepository;

    public ProfessorService() {
        professorRepository = new ProfessorRepository();
    }

    public void save(Document professor) {
        professorRepository.save(professor);
    }

    public Document login(String email, String senha) {
        return professorRepository.login(email, senha);
    }

    public Document ativar(String email) {
        professorRepository.ativar(email);
        return professorRepository.findByEmail(email);
    }

    public Document findByEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    public FindIterable<Document> findAll() {
        return professorRepository.findAll();
    }
}
