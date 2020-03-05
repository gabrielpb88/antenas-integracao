package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ProjetoRepository {

	private MongoDatabase db = Database.getConnection();
	private MongoCollection<Document> collection;

	public ProjetoRepository(){
		collection = db.getCollection("projeto");
	}

	public void save(Document projeto) {
		collection.insertOne(projeto);
	}

	public FindIterable<Document> findByTeacher(String email) {
		return collection.find(Filters.eq("responsavel-professor", email));
	}

	public FindIterable<Document> findByEmpresario(String email) {
		return collection.find(Filters.eq("responsavel-empresario", email));
	}

	public FindIterable<Document> findByCadi(String email) {
		return collection.find(Filters.eq("responsavel-cadi", email));
	}

	public FindIterable<Document> findByAluno(String email) {
		return collection.find(Filters.eq("responsavel-aluno", email));
	}

	public FindIterable<Document> findWithoutCadi() {
		return collection.find(Filters.eq("responsavel-cadi", ""));
	}

	public FindIterable<Document> findWithoutAluno() {
		return collection.find(Filters.eq("responsavel-aluno", ""));
	}

	public Document atribuirAlunoResponsavel(String email, String id) {
		collection.updateOne(Filters.eq("_id", new ObjectId(id)), Updates.set("responsavel-aluno", email));
		return findById(id);
	}

	public Document atribuirProfessorResponsavel(String email, String id) {
		collection.updateOne(Filters.eq("_id", new ObjectId(id)), Updates.set("responsavel-professor", email));
		return findById(id);
	}

	public Document update(Document projeto) {
		return collection.findOneAndUpdate(Filters.eq("_id", projeto.get("_id")), projeto);
	}

	public DeleteResult delete(Document project) {
		return collection.deleteOne(project);
	}

	public FindIterable<Document> findAll() {
		return collection.find();
	}

	public Document findById(String id) {
		return collection.find(Filters.eq("_id", new ObjectId(id))).first();
	}
}
