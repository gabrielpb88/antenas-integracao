package br.com.fatecsjc.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Database {
	
	private static MongoClient mongoClient;
	private static MongoDatabase db;

	private Database() {}
	
	/**
	 * Cria uma conexão com o banco de dados e retorna um objeto MongoDatabase
	 * @return MongoDatabase
	 */
	public static MongoDatabase getConnection() {
		if(db == null) {
			mongoClient = MongoClients.create("mongodb://localhost");
			db = mongoClient.getDatabase("projetoantenas");
		}
		
		return db;
	}
	
}
