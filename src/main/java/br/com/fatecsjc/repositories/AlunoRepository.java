package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class AlunoRepository {

    private MongoCollection<Document> collection;

    public AlunoRepository() {
        MongoDatabase db = Database.getDatabase();
        collection = db.getCollection("alunos");
    }

    public void save(Document aluno) {
        collection.insertOne(aluno);
    }

    public Document login(String email, String senha) {
        return collection.find(Filters.and(Filters.eq("email", email), Filters.eq("senha", senha))).first();
    }

    public Document update(Document aluno) {
        collection.updateOne(Filters.eq("_id", aluno.get("_id").toString()), aluno);
        return findByEmail(aluno.get("email").toString());
    }

    public Document findByEmail(String email) {
       return collection.find(Filters.eq("email", email)).first();
    }

    public FindIterable<Document> findAll() {
        return collection.find();
    }

}
