package br.com.fatecsjc.services;

import br.com.fatecsjc.repositories.ProjetoRepository;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ProjetoService {

	private ProjetoRepository projetoRepository;

	public ProjetoService(){
		projetoRepository = new ProjetoRepository();
	}

	public void save(Document projeto) {
		projetoRepository.save(projeto);
	}

	public FindIterable<Document> findByTeacher(String email) {
		return projetoRepository.findByTeacher(email);
	}

	public FindIterable<Document> findByEmpresario(String email) {
		return projetoRepository.findByEmpresario(email);
	}

	public FindIterable<Document> findByCadi(String email) {
		return projetoRepository.findByCadi(email);
	}

	public FindIterable<Document> findByAluno(String email) {
		return projetoRepository.findByAluno(email);
	}

	public FindIterable<Document> findWithoutCadi() {
		return projetoRepository.findWithoutCadi();
	}

	public FindIterable<Document> findWithoutAluno() {
		return projetoRepository.findWithoutAluno();
	}

	public Document atribuirAlunoResponsavel(String email, String id) {
		projetoRepository.atribuirAlunoResponsavel(email, id);
		return projetoRepository.findById(id);
	}

	public Document atribuirProfessorResponsavel(String email, String id) {
		projetoRepository.atribuirProfessorResponsavel(email, id);
		return projetoRepository.findById(id);
	}

	public Document atribuirCadiResponsavel(Document document) {
		ObjectId projetoId = document.getObjectId("_id");
		String emailCadi = document.getString("responsavel-cadi");
		System.out.println(projetoId + ": " + emailCadi);
		return projetoRepository.atribuirCadiResponsavel(emailCadi, projetoId);
	}

	public Document update(Document projeto) {
		return projetoRepository.update(projeto);
	}

	public DeleteResult delete(Document project) {
		return projetoRepository.delete(project);
	}

	public FindIterable<Document> findAll() {
		return projetoRepository.findAll();
	}

	public Document findById(String id) {
		return projetoRepository.findById(id);
	}

}
