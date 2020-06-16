package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ReuniaoRepository {

    private MongoCollection<Document> collection;

    public ReuniaoRepository(){
        MongoDatabase db = Database.getDatabase();
        db.getCollection("reuniao");
    }

    public void salvar(Document reuniao) {
        collection.insertOne(reuniao);
    }
}
