package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Empresario;
import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.utils.Jwt;
import br.com.fatecsjc.utils.TextUtils;
import br.com.fatecsjc.utils.emailService;
import com.mongodb.client.FindIterable;
import org.bson.Document;
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
    private String WhoIsauth;
    private ProjetoService projetoService;

    public EmpresarioController(Empresario empresarioModel) {
        this.empresarioModel = empresarioModel;
        this.projetoService = new ProjetoService();
    }

    public String getWhoIsauth() {
        return WhoIsauth;
    }

    public void setWhoIsauth(String whoIsauth) {
        WhoIsauth = whoIsauth;
    }

    /**
     * Gera um token de autenticação para o usuário
     */
    public void Auth() {
        post("/Auth", new Route() {
            @Override
            public Object handle(final Request request, final Response response) {

                try {
                    response.header("Access-Control-Allow-Origin", "*");

                    // set
                    JSONObject myjson = new JSONObject(request.body());
                    Jwt AuthEngine = new Jwt();

                    // try to find user
                    Document user = empresarioModel.findByEmail(myjson.getString("email"));

                    String email = user.getString("email");
                    String senhaDigitada = myjson.getString("senha");
                    String senhaArmazenada = user.getString("senha");
                    boolean usuarioAtivo = user.getBoolean("ativo");

                    if (email.length() > 0 && senhaDigitada.equals(senhaArmazenada) && usuarioAtivo) {
                        response.status(200);
                        return AuthEngine.GenerateJwt(email);
                    }
                    response.status(403);
                    return "Usuário inexistente ou inativo";

                } catch (JSONException ex) {
                    return "erro 500 " + ex;
                }
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
        post("/cadastroempresario", new Route() {
            @Override
            public Object handle(final Request request, final Response response) {
                try {
                    response.header("Access-Control-Allow-Origin", "*");
                    String jsonString = request.body();
                    Document userData = Document.parse(jsonString);

                    userData.append("ativo", false);

                    Document found = empresarioModel.findByEmail(userData.getString("email"));

                    if (found == null || found.isEmpty()) {
                        empresarioModel.save(userData);
                        new emailService(userData).sendSimpleEmail("Antenas - Sua confirmação de conta",
                                "Por favor, para confirmar sua conta, clique no link: ", "empresario");
                        return userData.toJson();
                    } else {
                        return "Email já cadastrado";
                    }
                } catch (JSONException ex) {
                    return "erro 500 " + ex;
                }
            }
        });
    }

    /**
     * Lista os empresarios
     */
    public void getEmpresarios() {
        get("/empresarios", new Route() {
            @Override
            public Object handle(final Request request, final Response response) {
                FindIterable<Document> empresariosFound = empresarioModel.findAll();

                return StreamSupport.stream(empresariosFound.spliterator(), false).map(Document::toJson)
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        });
    }

    /**
     * Faz requisição de login
     */
    public void loginEmpresario() {
        post("/loginempresario", new Route() {
            @Override
            public Object handle(final Request request, final Response response) {
                String jsonString = request.body();
                JSONObject jsonobj = new JSONObject(jsonString);
                Document found = empresarioModel.findByEmail(jsonobj.getString("email"));

                if (found == null) {
                    response.status(404);
                    return null;
                } else {
                    response.status(200);
                    return found.toJson();
                }
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

    /**
     * Lista os projetos do empresario
     * TODO: refatorar URL
     */
    public void getProjectByEmpresario() {
        get("/buscaprojetoporempresario", (req, res) -> {
            return TextUtils.converter(projetoService.findByEmpresario(req.queryString()));
        });
    }
}
