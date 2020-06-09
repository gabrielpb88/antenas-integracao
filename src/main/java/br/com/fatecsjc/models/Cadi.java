package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import br.com.fatecsjc.utils.TextUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Cadi {

	private MongoDatabase db;
	private MongoCollection<Document> cadiCollection;
	private MongoCollection<Document> projects;
	private MongoCollection<Document> empresarios;

	public Cadi(){
		db = Database.getConnection();
		cadiCollection = db.getCollection("cadi");
		projects = db.getCollection("projeto");
		empresarios = db.getCollection("empresario");
	}

	public String search(String chave, String valor) {
		return TextUtils.converter(projects.find(new Document(chave, valor)));
	}

	public Document searchEmpresario(String email) {
		return empresarios.find(new Document("email", email)).first();
	}

	public String buscaSemDono() {
		return TextUtils.converter(projects.find(new Document("responsavel-cadi", "")));
	}

	public void addCADI(Document cadi) {
		cadiCollection.insertOne(cadi);
	}

	public Document login(String email, String senha) {
		return cadiCollection.find(new Document("email", email).append("senha", senha)).first();
	}

	public Document ativarCadi(String email) {
		Document cadi = searchByEmail(email);
		cadi.replace("ativo", true);
		return updateCadi(cadi);
	}

	public Document searchByEmail(String email) {
		MongoCollection<Document> cadi = db.getCollection("cadi");
		Document found = cadi.find(new Document("email", email)).first();
		return found;
	}

	public void addReuniao(Document doc) {
		MongoCollection<Document> reuniao = db.getCollection("reuniao");
		reuniao.insertOne(doc);
	}

	// TODO: exemplo de preguiça
	public Document updateProjeto(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}

	public Document updateCadi(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("cadi");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	} 
}
