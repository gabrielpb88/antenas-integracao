package br.com.fatecsjc.models.dao;

import br.com.fatecsjc.config.Database;
import br.com.fatecsjc.models.entities.UsuarioCadi;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class UsuarioCadiDao {

    private MongoCollection<UsuarioCadi> collection;
    private MongoDatabase db = Database.getDatabase();

    public UsuarioCadiDao() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = db.getCollection("usuarios", UsuarioCadi.class).withCodecRegistry(pojoCodecRegistry);
    }

    public void save(UsuarioCadi usuario) {
        collection.insertOne(usuario);
    }

    public UsuarioCadi findByEmail(String email) {
        return collection.find(Filters.eq("email", email)).first();
    }

    public UpdateResult update(UsuarioCadi usuario){
        return collection.replaceOne(new Document("_id", usuario.getId()), usuario);
    }
}
