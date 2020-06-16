package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class ProfessorRepository {

    private MongoDatabase db = Database.getDatabase();
    private MongoCollection<Document> projetosCollection;
    private MongoCollection<Document> professorCollection;

    public ProfessorRepository() {
        projetosCollection = db.getCollection("projeto");
        professorCollection = db.getCollection("professor");
    }

    public void save(Document professor) {
        professorCollection.insertOne(professor);
    }

    public Document login(String email, String senha) {
        return professorCollection.find(Filters.and(Filters.eq("email", email), Filters.eq("senha", senha))).first();
    }

    public Document ativar(String email) {
        professorCollection.updateOne(Filters.eq("email", email), Updates.set("ativo", true));
        return findByEmail(email);
    }

    public Document findByEmail(String email) {
        return professorCollection.find(Filters.eq("email", email)).first();
    }

    public FindIterable<Document> findAll() {
        return professorCollection.find();
    }
}
