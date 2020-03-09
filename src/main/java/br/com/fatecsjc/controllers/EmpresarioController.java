package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Empresario;
import br.com.fatecsjc.services.EmpresarioService;
import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.services.exceptions.AccessForbiddenException;
import br.com.fatecsjc.services.exceptions.UserAlreadyExists;
import br.com.fatecsjc.utils.Jwt;
import br.com.fatecsjc.utils.TextUtils;
import br.com.fatecsjc.utils.EmailService;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static spark.Spark.get;
import static spark.Spark.post;

public class EmpresarioController {

    private Empresario empresarioModel;
    private ProjetoService projetoService;
    private EmpresarioService service;

    public EmpresarioController(Empresario empresarioModel) {
        this.empresarioModel = empresarioModel;
        this.projetoService = new ProjetoService();
        this.service = new EmpresarioService();
    }

    /**
     * Autentica o empresario e gera um token para o mesmo.
     */
    public void Autenticar() {
        post("/empresarios/login", (req, res) -> {
            Document dadosLogin = Document.parse(req.body());
            try {
                res.status(200);
                return service.login(dadosLogin);
            } catch (AccessForbiddenException e) {
                res.status(403);
                return e.getMessage();
            }
        });
    }

    /**
     * Verifica se o usuário está autenticado
     */
    public void IsAuth() {
        post("/is-auth", new Route() {
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

                        Document empresario = empresarioModel.findByEmail(emailOrNull);

                        if (empresario == null) {
                            response.status(404);
                            return false;
                        }

                        response.status(200);
                        return empresario.toJson();
                    }

                } catch (JSONException ex) {
                    return false;
                }
            }
        });
    }

    /**
     * Cadastra um novo usuario
     */
    public void cadastroEmpresario() {

        get("/empresarios", (req, res) ->{
            return TextUtils.converter(service.findAll());
        });

        post("/empresarios", (req, res) -> {
            Document empresario = Document.parse(req.body());

            try {
                return service.save(empresario).toJson();
            } catch (UserAlreadyExists e) {
                res.status(HttpStatus.CONFLICT_409);
                return e.getMessage();
            }
        });
    }

    /**
     * É chamado quando o usuario recebe o link de ativação no email
     */
    public void ativarUsuario() {
        get("/active/empresario/:email", new Route() {
            @Override
            public Object handle(final Request request, final Response response) {
                String email = new String(Base64.getDecoder().decode(request.params("email")));
                Document found = empresarioModel.findByEmail(email);
                found.replace("ativo", true);
                empresarioModel.update(found);
                if (!found.isEmpty()) {
                    response.redirect("http://localhost:8081/");
                }
                return null;
            }
        });
    }
}
