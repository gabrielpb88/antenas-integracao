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

public class Aluno {

	MongoDatabase db = Database.getConnection();

	public String search(String chave) {
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("chave", chave));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public String buscaPorDono(String emailAluno) {

		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-aluno", emailAluno));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	public String buscaProjetoSemAlunoResponsavel() {
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-aluno", ""));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		return foundJson;
	}

	/**
	 *
	 * @param emailAluno Email do aluno
	 * @param _id id do projeto ao qual o aluno será responsável
	 * @return Retorna o projeto
	 */
	public Document atribuirAlunoResponsavelParaUmProjeto(String emailAluno, String _id) {

		MongoCollection<Document> projects = db.getCollection("projeto");
		Document projeto = projects.find(new Document("_id", _id)).first();
		BasicDBObject searchQuery = new BasicDBObject().append("_id", _id);
		projeto.put("responsavel-professor", emailAluno);
		projects.replaceOne(searchQuery, projeto);
		return projeto;
	}

	public Document atribuir(String emailAluno, String _id) {
		MongoCollection<Document> projects = db.getCollection("projeto");
		Document found = projects.find(new Document("_id", _id)).first();
		if (found != null) {
			BasicDBObject searchQuery = new BasicDBObject().append("_id", _id);
			found.put("responsavel-aluno", emailAluno);
			projects.replaceOne(searchQuery, found);
			return found;
		} else
			return null;
	}



	/**
	 * Adiciona um novo aluno
	 * @param novoAluno Aluno a ser adicionado
	 */
	public void addAluno(Document novoAluno) {
		MongoCollection<Document> alunosCollection = db.getCollection("alunos");
		alunosCollection.insertOne(novoAluno);
	}

	/**
	 * Faz o login do aluno
	 * @param email email do aluno
	 * @param senha senha do aluno
	 * @return retorna um Documento contendo o aluno
	 */
	public Document login(String email, String senha) {
		MongoCollection<Document> alunos = db.getCollection("alunos");
		Document aluno = alunos.find(new Document("email", email).append("senha", senha)).first();

		return aluno;
	}


	/**
	 * Atualiza o aluno passado como parametro
	 * @param aluno a ser atualizado
	 * @return retorna o aluno atualizado
	 */
	public Document updateAluno(Document aluno) {
		MongoCollection<Document> alunos = db.getCollection("alunos");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", aluno.get("_id"));
		Bson newDocument = new Document("$set", aluno);
		return alunos.findOneAndUpdate(query, newDocument);
	}

	public Document procurarEmail(String email) {
		MongoCollection<Document> alunos = db.getCollection("alunos");
		Document found = alunos.find(new Document("email", email)).first();
		return found;
	}

	public FindIterable<Document> listaProjetos() {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find();
		return found;
	}

	public Document updateProjeto(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		Document found = projetos.find(new Document("chave", projeto.get("chave"))).first();
		if (found != null) {
			query.append("chave", projeto.get("chave"));
			Bson newDocument = new Document("$set", projeto);
			return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
		} else
			return null;
	}

	public Document submitProject(String id, Document projeto, String autores, String desc, String link) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		Document found = projetos.find(new Document("_id", id)).first();
		BasicDBObject searchQuery = new BasicDBObject().append("_id", id);
		projeto.put("fase", 6);
		projeto.put("descricao-breve", desc);
		projeto.put("link-externo-1", link);
		System.out.println(projeto);
		projetos.replaceOne(searchQuery, found);
		return found;
	}

	public List<String> listAlunos() {
		MongoCollection<Document> alunos = db.getCollection("alunos");
		FindIterable<Document> alunosF = alunos.find();
		List<String> listAlunos = new ArrayList<String>();
		for (Document proj : alunosF) {
			listAlunos.add(proj.toJson());
		}
		return listAlunos;
	}

	public void alterarId(String id, Document alteracao) {
		Document filter = new Document("id", id);
		MongoCollection<Document> alunos = db.getCollection("alunos");
		alunos.updateOne(filter, alteracao);
	}
}
