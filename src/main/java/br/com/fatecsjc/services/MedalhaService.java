package br.com.fatecsjc.services;

import br.com.fatecsjc.models.Medalha;
import br.com.fatecsjc.repositories.MedalhaRepository;
import com.mongodb.client.FindIterable;
import org.bson.types.ObjectId;

public class MedalhaService {

    private MedalhaRepository medalhaRepository;

    public MedalhaService() {
        medalhaRepository = new MedalhaRepository();
    }

    public void save(Medalha medalha) {
        medalhaRepository.save(medalha);
    }

    public void delete(Medalha medalha){ medalhaRepository.delete(medalha); }

    public Medalha update(Medalha medalha) {
        medalhaRepository.update(medalha);
        return medalhaRepository.findById(medalha.getId());
    }

    public FindIterable<Medalha> findAll() {
        return medalhaRepository.findAll();
    }

    public FindIterable<Medalha> findByCompetencia(String nome) {
        return medalhaRepository.findByCompetencia(nome);
    }
}
