package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Professor {

	private MongoDatabase db;
	private MongoCollection<Document> collection;

	public Professor(){
		db = Database.getDatabase();
		collection = db.getCollection("professor");
	}

//	public ArrayList<Document> myProjects(Document email) {
//		MongoCollection<Document> projetos = db.getCollection("projeto");
//
//		ArrayList<Document> projects = new ArrayList<Document>();
//
//		FindIterable<Document> found = projetos.find();
//
//		for (Document d : found) {
//			List<String> emails = (ArrayList<String>) d.get("responsavel-professor");
//
//			for (String emailFromProject : emails) {
//				String emailFromUser = (String) email.get("email");
//
//				if (emailFromProject.equals(emailFromUser)) {
//					projects.add(d);
//				}
//			}
//		}
//
//		return projects;
//	}
	
	public FindIterable<Document> myProjects(Document email) {
	MongoCollection<Document> projetos = db.getCollection("projeto");

	return projetos.find(new Document("responsavel-professor", (String) email.get("email")));
}

	public void addProfessor(Document professor) {
		collection.insertOne(professor);
	}

	public Document login(String email, String senha) {
		return collection.find(new Document("email", email).append("senha", senha)).first();
	}

	public UpdateResult ativarProfessor(String email) {
		Document prof = searchByEmail(email);
		prof.replace("ativo", true);
		return updateProfessor(prof);
	}

	public Document searchByEmail(String email) {
		MongoCollection<Document> prof = db.getCollection("professor");
		Document found = prof.find(new Document("email", email)).first();
		return found;

	}

	/* Update */
	public Document updateProjeto(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}

	public UpdateResult updateProfessor(Document professor) {
		return collection.replaceOne(new Document("_id", professor.get("_id")), professor);
	}

}
