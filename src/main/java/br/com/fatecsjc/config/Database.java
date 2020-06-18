package br.com.fatecsjc.config;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.BsonDocument;
import org.bson.Document;

public class Database {
	
	private static MongoClient mongoClient;
	private static MongoDatabase db;

	private Database() {}
	
	/**
	 * Cria uma conexão com o banco de dados e retorna um objeto MongoDatabase
	 * @return MongoDatabase
	 */
	public static MongoDatabase getDatabase() {
		if(db == null) {
			mongoClient = MongoClients.create("mongodb://172.17.0.2");
			db = mongoClient.getDatabase("projetoantenas");
			db.getCollection("usuarios").createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
		}
		
		return db;
	}
	
}
