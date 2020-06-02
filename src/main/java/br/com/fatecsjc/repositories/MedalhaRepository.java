package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class MedalhaRepository {

    private MongoCollection<Document> collection;

    public MedalhaRepository() {
        MongoDatabase db = Database.getConnection();
        collection = db.getCollection("medalhas");
    }

    public FindIterable<Document> findByCompetencia(String competencia) {
        return collection.find(Filters.eq("nome", competencia));
    }

    public FindIterable<Document> findAll() {
        return collection.find();
    }

    public void save(Document medalha) {
        collection.insertOne(medalha);
    }

    public UpdateResult update(Document medalha) {
        return collection.updateOne(Filters.eq("_id", medalha.get("_id").toString()), medalha);
    }

    public DeleteResult delete(Document medalha){
        return collection.deleteOne(medalha);
    }


}
