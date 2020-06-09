package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Cadi;
import br.com.fatecsjc.services.CadiService;
import br.com.fatecsjc.services.ProfessorService;
import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.utils.Jwt;
import br.com.fatecsjc.utils.EmailService;
import br.com.fatecsjc.utils.TextUtils;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Base64;

import static spark.Spark.get;
import static spark.Spark.post;

public class CadiController {

	private Cadi model;
	private CadiService service;
	private ProjetoService projetoService;
	private ProfessorService professorService;

	public CadiController(Cadi model) {
		super();
		this.model = model;
		this.service = new CadiService();
		this.projetoService = new ProjetoService();
		this.professorService = new ProfessorService();
	}

	public void Auth() { // Gera um token de autenticacaoo para o usuario
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

	public void ativarUsuario() { // chamado quando o usuario recebe o link de ativacao no email
		get("/active/cadi/:email", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = new String(Base64.getDecoder().decode(request.params("email")));
				Document found = model.ativarCadi(email);
				if (!found.isEmpty()) {
					response.redirect("http://localhost:8081/cadi/index.html");
				}
				return null;
			}
		});
	}

	public void loginCadi() {
		post("/cadi", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				JSONObject json = new JSONObject(request.body());
				String email = json.getString("email");
				String senha = json.getString("senha");
				try {
					Document cadi = model.login(email, senha);

					if ((Boolean) cadi.get("ativo") == true) {
						return cadi.toJson();
					}
					return null;
				} catch (NullPointerException e) {
					return null;
				}
			}
		});
	}

	public void atribuirProjeto() {
		post("/cadi/semresponsavelcadi", (Request request, Response response) -> {
			projetoService.atribuirCadiResponsavel(Document.parse(request.body()));
			return model.buscaSemDono();
		});
	}

	public void inserirCADI() {

		post("/cadicadastro", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					String jsonString = request.body();
					Document userData = Document.parse(jsonString);
					userData.append("ativo", false);
					Document found = model.searchByEmail(userData.getString("email"));
					if (found == null || found.isEmpty()) {
						model.addCADI(userData);
						new EmailService(userData).sendSimpleEmail("Antenas - Sua confirma��o de conta",
								"Por favor, para confirmar sua conta, clique no link: ", "cadi");
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

	public void atualizaCadi() {
		post("/updateCadi", (Request request, Response response) -> {
			model.updateCadi(Document.parse(request.body()));
			return model.buscaSemDono();
		});
	}

	public void search() {
		get("/search", (request, response) -> {
			return model.search(request.queryParams("chave"), request.queryParams("valor"));
		});

		get("/searchEmpresario/:email", (request, response) -> {
			System.out.println(model.searchEmpresario(request.params("email")).toJson());
			return model.searchEmpresario(request.params("email")).toJson();
		});

		post("/usuarioLogado", (request, response) -> {
			JSONObject json = new JSONObject(request.body());
			String email = json.getString("email");
			return model.searchByEmail(email).toJson();
		});

		get("/dono", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = request.queryString();
				return TextUtils.converter(projetoService.findByCadi(email));
			}
		});
		get("/projetos/semcadi", (request, response) -> {
			return TextUtils.converter(projetoService.findWithoutCadi());
		});

		post("/ ", (request, response) -> {
			Document projetoComProfessor = Document.parse(request.body());
			model.updateProjeto(projetoComProfessor);
			return projetoComProfessor.toJson();
		});

		post("/putCadi", (request, response) -> {
			Document projetoComCadi = Document.parse(request.body());
			model.updateProjeto(projetoComCadi);
			return projetoComCadi.toJson();
		});
	}

	public void inserirReuniao() {
		get("/reuniao", (Request request, Response response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			Document reuniao = Document.parse(request.body());
			model.addReuniao(reuniao);
			return reuniao.toJson();
		});
	}

}
