package br.com.fatecsjc.models;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.DeleteResult;

import br.com.fatecsjc.config.Database;

public class ProjetoModel {

	MongoDatabase db = Database.getConnection();

	public void save(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		projetos.insertOne(projeto);
	}

	public DeleteResult delete(Document project) {
		MongoCollection<Document> projectsFound = db.getCollection("projeto");
		return projectsFound.deleteOne(project);
	}

	public Document update(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}

	public FindIterable<Document> findAll() {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> todos = projetos.find();
		return todos;
	}
}
