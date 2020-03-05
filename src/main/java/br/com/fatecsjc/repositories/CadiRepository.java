package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class CadiRepository {

	private MongoCollection<Document> collection;

	public CadiRepository(){
		MongoDatabase db = Database.getConnection();
		collection = db.getCollection("cadi");
	}

	public FindIterable<Document> findbyField(String chave, String valor) {
		 return collection.find(Filters.eq(chave, valor));
	}

	public void save(Document cadi) {
		collection.insertOne(cadi);
	}

	public Document login(String email, String senha) {
		return collection.find(Filters.and(Filters.eq("email", email),Filters.eq("senha", senha))).first();
	}

	public Document ativar(String email) {
		collection.updateOne(Filters.eq("email", email), Updates.set("ativo", true));
		return findByEmail(email);
	}

	public Document findByEmail(String email) {
		return collection.find(Filters.eq("email", email)).first();
	}

	public FindIterable findAll() {
		return collection.find();
	}

	public Document update(Document cadi) {
		collection.updateOne(Filters.eq("_id", cadi.get("_id")), cadi);
		return findByEmail(cadi.get("email").toString());
	}

}
