package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Projeto {

	private MongoDatabase db;
	private MongoCollection<Document> collection;

	public Projeto(){
		db  = Database.getConnection();
		collection = db.getCollection("projeto");
	}

	public void save(Document projeto) {
		collection.insertOne(projeto);
	}

	public Document getProject(String _id) {
		return collection.find(new Document("_id", _id)).first();
	}
}
