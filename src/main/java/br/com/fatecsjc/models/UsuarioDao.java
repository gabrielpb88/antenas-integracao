package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class UsuarioDao {

    private MongoCollection<Usuario> collection;
    private MongoDatabase db = Database.getDatabase();

    public UsuarioDao() {
        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = db.getCollection("usuarios", Usuario.class).withCodecRegistry(pojoCodecRegistry);
    }

    public void salvar(Usuario usuario){
        collection.insertOne(usuario);
    }
}
