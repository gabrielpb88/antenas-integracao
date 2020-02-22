package br.com.fatecsjc.config;

import com.mongodb.MongoClient;
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
			mongoClient = new MongoClient( "172.17.0.2" );
			db = mongoClient.getDatabase("app");
		}
		
		return db;
	}
	
}
