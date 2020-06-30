package br.com.fatecsjc.models.dao;

import br.com.fatecsjc.config.Database;
import br.com.fatecsjc.models.entities.Medalha;
import br.com.fatecsjc.models.entities.Usuario;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MedalhaDao {

    private MongoCollection<Medalha> collection;
    private MongoDatabase db = Database.getDatabase();

    public MedalhaDao() {
        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = db.getCollection("medalhas", Medalha.class).withCodecRegistry(pojoCodecRegistry);
    }

    public FindIterable<Medalha> findAll(){
        return collection.find();
    }

    public Medalha findById(ObjectId id){
        return collection.find(Filters.eq("_id", id)).first();
    }

    public FindIterable<Medalha> findByCompetencia(String competencia){
        return collection.find(Filters.eq("competencia", competencia));
    }

    public boolean exists(String competencia){
        return collection.find(Filters.eq("competencia", competencia)).first() != null;
    }

    public void save(Medalha medalha){
        collection.insertOne(medalha);
    }

    public void update(Medalha medalha){ collection.replaceOne(Filters.eq(medalha.getId()), medalha); }
}
