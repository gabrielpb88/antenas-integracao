package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.CadiRepository;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class CadiService {

	private CadiRepository cadiRepository;

	public CadiService(){
		cadiRepository = new CadiRepository();
	}

	public void save(Document cadi) {
		cadiRepository.save(cadi);
	}

	public Document login(String email, String senha) {
		return cadiRepository.login(email, senha);
	}

	public Document ativar(String email) {
		cadiRepository.ativar(email);
		return cadiRepository.findByEmail(email);
	}

	public Document findByEmail(String email) {
		return cadiRepository.findByEmail(email);
	}

	public FindIterable findAll() {
		return cadiRepository.findAll();
	}

	public Document update(Document cadi) {
		cadiRepository.update(cadi);
		return cadiRepository.findByEmail(cadi.get("email").toString());
	}

}
