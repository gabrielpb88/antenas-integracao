package br.com.fatecsjc.repositories;

import br.com.fatecsjc.config.Database;
import br.com.fatecsjc.models.entities.Medalha;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MedalhaRepository {

    private MongoCollection<Medalha> collection;

    public MedalhaRepository() {
        MongoDatabase db = Database.getDatabase();

        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = db.getCollection("medalhas", Medalha.class).withCodecRegistry(pojoCodecRegistry);
    }

    public FindIterable<Medalha> findByCompetencia(String competencia) {
        return collection.find(Filters.eq("competencia", competencia));
    }

    public Medalha findById(ObjectId id) {
        return collection.find(Filters.eq("_id", id)).first();
    }

    public FindIterable<Medalha> findAll() {
        return collection.find();
    }

    public void save(Medalha medalha) {
        collection.insertOne(medalha);
    }

    public UpdateResult update(Medalha medalha) {
        return collection.replaceOne(Filters.eq("_id", medalha.getId()), medalha);
    }

    public DeleteResult delete(Medalha medalha){
        return collection.deleteOne(Filters.eq("_id", medalha.getId()));
    }
}
