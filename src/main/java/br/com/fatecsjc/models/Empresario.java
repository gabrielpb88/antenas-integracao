package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Empresario {

	private MongoDatabase db;
	private MongoCollection<Document> collection;

	public Empresario(){
		db = Database.getDatabase();
		collection = db.getCollection("empresario");
	}

	public void save(Document empresario) {
		collection.insertOne(empresario);
	}

	public Document update(Document empresario) {
		MongoCollection<Document> projetos = db.getCollection("empresario");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", empresario.get("_id"));
		Bson newDocument = new Document("$set", empresario);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}

	public FindIterable<Document> findAll() {
		return collection.find();
	}

	public Document findByEmail(String email) {
		return collection.find(new Document("email", email)).first();
	}

}
