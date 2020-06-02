package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.MedalhaRepository;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class MedalhaService {

    private MedalhaRepository medalhaRepository;

    public MedalhaService() {
        medalhaRepository = new MedalhaRepository();
    }

    public void save(Document medalha) {
        medalhaRepository.save(medalha);
    }

    public void delete(Document medalha){ medalhaRepository.delete(medalha); }

    public Document update(Document medalha) {
        medalhaRepository.update(medalha);
        return medalhaRepository.findById(medalha);
    }

    public FindIterable<Document> findAll() {
        return medalhaRepository.findAll();
    }

    public FindIterable<Document> findByCompetencia(String nome) {
        return medalhaRepository.findByCompetencia(nome);
    }
}
