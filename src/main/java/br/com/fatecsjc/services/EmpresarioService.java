package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.EmpresarioRepository;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class EmpresarioService {

	private EmpresarioRepository empresarioRepository;

	public EmpresarioService(){
	    empresarioRepository = new EmpresarioRepository();
	}

	public void save(Document empresario) {
		empresarioRepository.save(empresario);
	}

	public Document update(Document empresario) {
		empresarioRepository.update(empresario);
		return empresarioRepository.findByEmail(empresario.get("email").toString());
	}

	public FindIterable<Document> findAll() {
		return empresarioRepository.findAll();
	}

}
