package br.com.fatecsjc.controllers;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.json.JSONException;

import com.mongodb.client.FindIterable;

import br.com.fatecsjc.models.ProjetoModel;
import spark.Request;
import spark.Response;
import spark.Route;

public class ProjetoController {

	private ProjetoModel projetoModel;
	private String WhoIsauth;

	public ProjetoController(ProjetoModel projetoModel) {
		this.projetoModel = projetoModel;
	}

	public String getWhoIsauth() {
		return WhoIsauth;
	}

	public void setWhoIsauth(String whoIsauth) {
		WhoIsauth = whoIsauth;
	}
		
	/**
	 * Cadastra um novo projeto
	 */
	public void cadastroProjeto() { 
		post("/cadastroprojeto", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					response.header("Access-Control-Allow-Origin", "*");
					String jsonString = request.body();

					Document project = Document.parse(jsonString);
					projetoModel.save(project);
					
					return project.toJson();
				} catch (JSONException ex) {
					return "erro 500 " + ex;
				}
			}
		});
	}
	
	/**
	 * Apaga um projeto
	 */
	public void deletaProjeto() { 
		post("/deletaProjeto", new Route() {
			@Override
			public Boolean handle(final Request request, final Response response) {
				try {
					response.header("Access-Control-Allow-Origin", "*");
					return projetoModel.delete( Document.parse( request.body() ) ).getDeletedCount() > 0;

				}catch(Exception ex){ throw ex; }
			}
		});
	}

	/**
	 * // Atualiza um projeto
	 */
	public void atualizaProjeto() { 
		post("/atualizaProjeto", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					response.header("Access-Control-Allow-Origin", "*");
					return projetoModel.update(Document.parse( request.body() )) == null? "projeto n�o encontrado": "projeto deletado";
				}catch(Exception ex) { throw ex; }
			}
		});
	}

	/**
	 *  Lista os projetos
	 */
	public void getProjetos() { 
		get("/projetos", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				 FindIterable<Document> projectsFound = projetoModel.findAll();

				 return StreamSupport.stream(projectsFound.spliterator(), false)
			        .map(Document::toJson)
			        .collect(Collectors.joining(", ", "[", "]"));
			}
		});
	}

}
