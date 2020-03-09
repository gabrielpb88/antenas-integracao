package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.EmpresarioRepository;
import br.com.fatecsjc.services.exceptions.AccessForbiddenException;
import br.com.fatecsjc.services.exceptions.UserAlreadyExists;
import br.com.fatecsjc.utils.EmailService;
import br.com.fatecsjc.utils.Jwt;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

public class EmpresarioService {

	private EmpresarioRepository empresarioRepository;

	public EmpresarioService(){
	    empresarioRepository = new EmpresarioRepository();
	}

	public Document save(Document empresario) {
		String email = empresario.getString("email");
		if(findByEmail(email) != null){
			throw new UserAlreadyExists();
		}
		empresario.put("ativo", false);

		new EmailService(empresario).sendSimpleEmail("Antenas - Sua confirmação de conta",
				"Por favor, para confirmar sua conta, clique no link: ", "empresario");

		empresarioRepository.save(empresario);
		return findByEmail(email);
	}

	public Document update(Document empresario) {
		empresarioRepository.update(empresario);
		return empresarioRepository.findByEmail(empresario.get("email").toString());
	}

	public Document findByEmail(String email){
		return empresarioRepository.findByEmail(email);
	}

	public FindIterable<Document> findAll() {
		return empresarioRepository.findAll();
	}

	public String login(Document dadosLogin) {

		String email = dadosLogin.getString("email");
		String senha = dadosLogin.getString("senha");

		Document empresario = empresarioRepository.findByEmail(email);

		if(empresario != null && empresario.getBoolean("ativo")
				&& empresario.getString("email").equals(email)
				&& empresario.getString("senha").equals(senha)){
			Jwt authEngine = new Jwt();
			return authEngine.GenerateJwt(email);
		}
		throw new AccessForbiddenException();
	}
}
