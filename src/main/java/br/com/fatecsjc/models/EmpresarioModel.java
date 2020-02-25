package br.com.fatecsjc.models;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;

import br.com.fatecsjc.config.Database;

public class EmpresarioModel {

	MongoDatabase db = Database.getConnection();

	public void save(Document empresario) {
		MongoCollection<Document> empresarios = db.getCollection("empresario");
		empresarios.insertOne(empresario);
	}

	public Document update(Document empresario) {
		MongoCollection<Document> projetos = db.getCollection("empresario");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", empresario.get("_id"));
		Bson newDocument = new Document("$set", empresario);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}

	public FindIterable<Document> findAll() {
		MongoCollection<Document> empresarios = db.getCollection("empresario");
		FindIterable<Document> todos = empresarios.find();

		for (Document empresario : todos) {
			System.out.println(empresario);
		}
		return todos;
	}

	public Document findByEmail(String email) {
		MongoCollection<Document> users = db.getCollection("empresario");
		Document found = users.find(new Document("email", email)).first();
		return found;
	}

	public FindIterable<Document> getProjectByEmpresario(String email) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find(new Document("responsavel-empresario", email));

		return found;
	}
}
