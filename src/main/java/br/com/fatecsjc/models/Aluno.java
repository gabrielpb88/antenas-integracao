package br.com.fatecsjc.models;

import br.com.fatecsjc.config.Database;
import br.com.fatecsjc.models.dao.MedalhaDao;
import br.com.fatecsjc.models.dao.UsuarioDao;
import br.com.fatecsjc.models.entities.Medalha;
import br.com.fatecsjc.models.entities.MedalhaAtribuida;
import br.com.fatecsjc.models.entities.UsuarioAluno;
import br.com.fatecsjc.models.entities.UsuarioProfessor;
import br.com.fatecsjc.utils.TextUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Aluno {

	private MongoDatabase db;
	private MongoCollection<Document> alunos;
	private MongoCollection<Document> projects;
	private UsuarioDao professorDao = new UsuarioDao(UsuarioProfessor.class);
	private UsuarioDao alunoDao = new UsuarioDao(UsuarioAluno.class);
	private MedalhaDao medalhaDao = new MedalhaDao();

	public Aluno(){
		db = Database.getDatabase();
		alunos = db.getCollection("alunos");
		projects = db.getCollection("projeto");
	}

	public void atribuirMedalha(String emailAluno, String emailProfessor, Medalha medalha){
		UsuarioAluno aluno = (UsuarioAluno) alunoDao.findByEmail(emailAluno);

		MedalhaAtribuida medalhasAtribuida = new MedalhaAtribuida();
		medalhasAtribuida.setMedalha(medalhaDao.findById(medalha.getId()));
		medalhasAtribuida.setProfessor(emailProfessor);

		aluno.getMedalhas().add(medalhasAtribuida);
		alunoDao.update(aluno);
	}

	public String search(String chave) {
		return TextUtils.converter(projects.find(new Document("chave", chave)));
	}

	public String buscaPorDono(String emailAluno) {
		return TextUtils.converter(projects.find(new Document("responsavel-aluno", emailAluno)));
	}

	public String buscaProjetoSemAlunoResponsavel() {
		return TextUtils.converter(projects.find(new Document("responsavel-aluno", "")));
	}

	public Document atribuir(String emailAluno, String id) {
		Document found = projects.find(new Document("_id", id)).first();
		if (found != null) {
			found.put("responsavel-aluno", emailAluno);
			projects.replaceOne(new Document("_id", id), found);
		}
		return found;
	}

	public void salvar(Document novoAluno) {
		alunos.insertOne(novoAluno);
	}

	public Document login(String email, String senha) {
		return alunos.find(new Document("email", email).append("senha", senha)).first();
	}

	public UpdateResult updateAluno(Document aluno) {
		return alunos.replaceOne(new Document("_id", aluno.get("_id")), aluno);
	}

	public Document findByEmail(String email) {
		return alunos.find(new Document("email", email)).first();
	}

	public Document updateProjeto(Document projeto) {
		BasicDBObject query = new BasicDBObject();
		Document found = projects.find(new Document("chave", projeto.get("chave"))).first();
		if (found != null) {
			query.append("chave", projeto.get("chave"));
			Bson newDocument = new Document("$set", projeto);
			return projects.findOneAndUpdate(query, newDocument);
		} else
			return null;
	}

	public Document submitProject(String id, Document projeto, String desc, String link) {
		Document found = projects.find(new Document("_id", id)).first();
		projeto.put("fase", 6);
		projeto.put("descricao-breve", desc);
		projeto.put("link-externo-1", link);
		projects.replaceOne(new Document("_id", id), found);
		return found;
	}
}
