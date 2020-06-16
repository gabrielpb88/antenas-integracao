package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class EmpresarioRepository {

	private MongoCollection<Document> collection;

	public EmpresarioRepository(){
		MongoDatabase db = Database.getDatabase();
		collection = db.getCollection("empresario");
	}

	public void save(Document empresario) {
		collection.insertOne(empresario);
	}

	public Document update(Document empresario) {
		collection.replaceOne(Filters.eq("_id", empresario.get("_id")), empresario);
		return findByEmail(empresario.get("email").toString());
	}

	public FindIterable<Document> findAll() {
		return collection.find();
	}

	public Document findByEmail(String email) {
		return collection.find(Filters.eq("email", email)).first();
	}
}
