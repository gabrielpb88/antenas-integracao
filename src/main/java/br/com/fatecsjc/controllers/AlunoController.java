package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Aluno;
import br.com.fatecsjc.models.Projeto;
import br.com.fatecsjc.services.AlunoService;
import br.com.fatecsjc.utils.EmailService;
import br.com.fatecsjc.utils.Jwt;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Base64;

import static spark.Spark.*;

public class AlunoController {

	private Aluno alunoModel;
	private Projeto projeto;
	private AlunoService alunoService;

	public AlunoController(Aluno model) {
		this.alunoModel = model;
		this.projeto = new Projeto();
		this.alunoService = new AlunoService();
	}

	public void loginAluno() {
		post("/aluno/auth", (Request req, Response res) -> {
				try {
					JSONObject body = new JSONObject(req.body());
					Jwt geradorJwt = new Jwt();

					Document aluno = alunoService.login(body.getString("email"), body.getString("senha"));

					if(aluno != null && aluno.getBoolean("ativo")){
						res.status(200);
						return geradorJwt.GenerateJwt(body.getString("email"));
					} else {
						res.status(403);
						return null;
					}
				} catch (JSONException ex) {
					return "erro 500 " + ex;
				}
			});
	}

	public void validaAluno() { // Verifica se o usuário está autenticado
		post("/valida-aluno", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {

				try {
					// setting
					JSONObject myjson = new JSONObject(request.body());
					Jwt AuthEngine = new Jwt();

					// try to find user
					String emailOrNull = AuthEngine.verifyJwt((myjson.getString("token")));
					if (emailOrNull == null) {
						response.status(404);
						return false;
					} else {

						Document aluno = alunoModel.findByEmail(emailOrNull);

						if (aluno == null) {
							response.status(404);
							return false;
						}

						response.status(200);
						return aluno.toJson();
					}

				} catch (JSONException ex) {
					return false;
				}
			}
		});
	}

	public void ativarUsuario() { // Link de ativacao do cadastro por email
		get("/active/aluno/:email", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = new String(Base64.getDecoder().decode(request.params("email")));
				Document found = alunoModel.findByEmail(email);
				found.replace("ativo", true);
				alunoModel.updateAluno(found);
				if (!found.isEmpty()) {
					response.redirect("/aluno"); // 8081
				}

				return null;
			}
		});
	}

	public void atribuirProjeto() {
		post("/add-projeto", (Request request, Response response) -> {
			Document json = Document.parse(request.body());
			try {
				Document retorno = alunoModel.updateProjeto(json);
				if (retorno != null)
					return retorno;
				else
					return false;
			} catch (NullPointerException e) {
				return null;
			}
		});

		put("/add-projeto", (Request request, Response response) -> {
			Document json = Document.parse(request.body());
			try {
				Document retorno = alunoModel.updateProjeto(json);
				if (retorno != null)
					return retorno;
				else
					return false;
			} catch (NullPointerException e) {
				return null;
			}
		});
	}

	public void atribuirMedalha(){
		put("/aluno", (Request request, Response response) -> {
			Document body = Document.parse(request.body());
			return alunoModel.updateAluno(body);
		});
	}

	public void cadastroAluno() {
		post("/aluno-cadastro", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					Document dadosAluno = Document.parse(request.body());

					dadosAluno.append("ativo", false);

					Document emailJaExiste = alunoModel.findByEmail(dadosAluno.getString("email"));

					if (emailJaExiste == null || emailJaExiste.isEmpty()) {
						alunoModel.salvar(dadosAluno);
						new EmailService(dadosAluno).sendSimpleEmail("Antenas - Sua confirma��o de conta",
								"Por favor, para confirmar sua conta, clique no link:", "aluno");
						return dadosAluno.toJson();
					} else {
						return "Email ja cadastrado!";
					}
				} catch (JSONException ex) {
					return "erro 500 " + ex;
				}
			}
		});
	}

	public void search() {
		get("/searchaluno/:id", (request, response) -> {
			return alunoModel.search(request.params(":id"));
		});

		get("/dono/:email", (request, response) -> {
			String ret = alunoModel.buscaPorDono(request.params(":email"));
			return ret;
		});

		get("/alunos/:email", (req, res) -> {
			return alunoService.findByEmail(req.params(":email")).toJson();
		});

		get("/semdono", (request, response) -> {
			return alunoModel.buscaProjetoSemAlunoResponsavel();
		});

		get("/put", (request, response) -> {
			return alunoModel.atribuir(request.queryParams("emailAluno"), request.queryParams("_id"));
		});

	}

	public void entregaProjeto() {
		post("/entregar", (req, res) -> {
			Document project = Document.parse(req.body());
			String id = project.getString("id");
			String descricao = project.getString("descricao");
			String linkGitHub = project.getString("link");
			Document project_id = projeto.getProject(id);
			return alunoModel.submitProject(id, project_id, descricao, linkGitHub);
		});
	}

}
