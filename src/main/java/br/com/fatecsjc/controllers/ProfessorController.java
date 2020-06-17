package br.com.fatecsjc.controllers;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import br.com.fatecsjc.models.Professor;
import br.com.fatecsjc.services.ProfessorService;
import br.com.fatecsjc.utils.TextUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.fatecsjc.utils.Jwt;
import br.com.fatecsjc.utils.EmailService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ProfessorController {

	private Professor model;
	private ProfessorService service;

	public ProfessorController(Professor model) {
		super();
		this.model = model;
		service = new ProfessorService();
	}

	public void initiate() {
		get("/professores", (req, res) -> {
			return TextUtils.converter(service.findAll());
		});
	}

	public void Auth() { // Gera um token de autentica��o para o usu�rio
		post("/Auth", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {

				try {
					response.header("Access-Control-Allow-Origin", "*");

					// set
					JSONObject myjson = new JSONObject(request.body());
					Jwt AuthEngine = new Jwt();

					// try to find user
					Document user = model.searchByEmail(myjson.getString("email"));

					String email = user.getString("email");
					String senhaDigitada = myjson.getString("senha");
					String senhaArmazenada = user.getString("senha");
					boolean usuarioAtivo = user.getBoolean("ativo");

					if (email.length() > 0 && senhaDigitada.equals(senhaArmazenada) && usuarioAtivo) {
						response.status(200);
						return AuthEngine.GenerateJwt(email);
					}
					response.status(403);
					return "Usu�rio inexistente ou inativo";

				} catch (JSONException ex) {
					return "erro 500 " + ex;
				}
			}
		});
	}

	public void ativarUsuario() { // � chamado quando o usuario recebe o link de ativa��o no email
		get("/active/professor/:email", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = new String(Base64.getDecoder().decode(request.params("email")));
				UpdateResult found = model.ativarProfessor(email);
				if (found.wasAcknowledged()) {
					response.redirect("http://localhost:8081/professor/index.html");
				}
				return null;
			}
		});
	}

	public void loginProfessor() {
		post("/professor", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				JSONObject json = new JSONObject(request.body());
				String email = json.getString("email");
				String senha = json.getString("senha");
				try {
					Document professor = model.login(email, senha);

					if ((Boolean) professor.get("ativo") == true) {
						return professor.toJson();
					}
					return null;
				} catch (NullPointerException e) {
					return null;
				}
			}
		});
	}

	public void updateProjetoProfessor() {
		post("/updateProjetoProfessor", (Request request, Response response) -> {
			model.updateProjeto(Document.parse(request.body()));
			return request.body();
		});
	}

	public void inserirProfessor() {
		post("/professorcadastro", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					response.header("Access-Control-Allow-Origin", "*");
					String jsonString = request.body();
					Document userData = Document.parse(jsonString);

					userData.append("ativo", false);

					Document found = model.searchByEmail(userData.getString("email"));

					if (found == null || found.isEmpty()) {
						model.addProfessor(userData);
						new EmailService(userData).sendSimpleEmail("Antenas - Sua confirmação de conta",
								"Por favor, para confirmar sua conta, clique no link: ", "professor");
						return userData.toJson();
					} else {
						return "Email j� cadastrado";
					}
				} catch (Exception ex) {
					return "erro 500 " + ex;
				}
			}
		});
	}

	public void atualizaProfessor() {
		post("/updateProfessor", (Request request, Response response) -> {
			model.updateProfessor(Document.parse(request.body()));
			return true;
		});
	}

	public void searchprofessor() {
		post("/professorLogado", (request, response) -> {
			JSONObject json = new JSONObject(request.body());
			String email = json.getString("email");
			return model.searchByEmail(email).toJson();
		});

		/* restornar meus projetos que fa�o parte */
		get("/myprojects", (Request request, Response response) -> {
			String email = request.queryString();
			return TextUtils.converter(model.myProjects(new Document("email", email)));
		});

	}

}
