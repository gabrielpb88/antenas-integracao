package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Cadi {

	private MongoDatabase db = Database.getConnection();

	public String search(String chave, String valor) {
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document(chave, valor));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public String searchUsuario(String chave, String valor) {
		MongoCollection<Document> projects = db.getCollection("cadi");
		FindIterable<Document> found = projects.find(new Document(chave, valor));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public Document searchEmpresario(String email) {
		MongoCollection<Document> empresarios = db.getCollection("empresario");
		Document found = empresarios.find(new Document("email", email)).first();
		return found;
	}

	public String buscaPorDono(String email) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find(new Document("responsavel-cadi", email));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public String buscaSemDono() {
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-cadi", ""));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public void addCADI(Document doc) {
		MongoCollection<Document> researches = db.getCollection("cadi");
		researches.insertOne(doc);
	}

	public void addProjeto(Document doc) {
		MongoCollection<Document> projeto = db.getCollection("projeto");
		projeto.insertOne(doc);
	}

	public void addProfessores(Document doc) {
		MongoCollection<Document> professor = db.getCollection("professor");
		professor.insertOne(doc);
	}

	public Document login(String email, String senha) {
		MongoCollection<Document> cadi = db.getCollection("cadi");
		Document found = cadi.find(new Document("email", email).append("senha", senha)).first();
		return found;
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

	public String listaProjetos() {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find();
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public List<String> listCadi() {
		MongoCollection<Document> cadiF = db.getCollection("cadi");
		FindIterable<Document> cadi = cadiF.find();
		List<String> listCadi = new ArrayList<String>();
		for (Document proj : cadi) {
			listCadi.add(proj.toJson());
		}
		return listCadi;
	}

	public void alterarId(String id, Document alteracao) {
		Document filter = new Document("id", id);
		MongoCollection<Document> cadiF = db.getCollection("cadi");
		cadiF.updateOne(filter, alteracao);
	}

	public void addReuniao(Document doc) {
		MongoCollection<Document> reuniao = db.getCollection("reuniao");
		reuniao.insertOne(doc);
	}

	/* Update */
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
