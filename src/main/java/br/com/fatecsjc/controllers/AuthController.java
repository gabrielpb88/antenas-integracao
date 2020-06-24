package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Aluno;
import br.com.fatecsjc.models.Projeto;
import br.com.fatecsjc.models.dao.UsuarioDao;
import br.com.fatecsjc.models.entities.Usuario;
import br.com.fatecsjc.models.entities.UsuarioAluno;
import br.com.fatecsjc.models.entities.UsuarioProfessor;
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

import com.google.gson.Gson;

import static spark.Spark.*;

public class AuthController {

    private UsuarioDao usuarioDao;
    private Gson gson;

    public AuthController() {
        this.usuarioDao = new UsuarioDao(Usuario.class);
        this.gson = new Gson();
    }

    public void login() {

        get("/login", (Request req, Response res) -> {
            return "Respondendo";
        });

        post("/login", (Request req, Response res) -> {
            try {
                JSONObject body = new JSONObject(req.body());
                Jwt geradorJwt = new Jwt();
                Usuario usuario = usuarioDao.findByEmail(body.getString("email"));
                System.out.println(usuario);

                // Document usuario = alunoService.login(body.getString("email"),
                // body.getString("senha"));

                // if (usuario != null && usuario.getBoolean("ativo")) {
                // res.status(200);
                // return geradorJwt.GenerateJwt(body.getString("email"));
                // } else {
                // res.status(403);
                // return null;
                // }
                return "rodou";
            } catch (JSONException ex) {
                return "erro 500 " + ex;
            }
        });
    }

    // public void validaAluno() { // Verifica se o usuário está autenticado
    // post("/valida-aluno", new Route() {
    // @Override
    // public Object handle(final Request request, final Response response) {

    // try {
    // // setting
    // JSONObject myjson = new JSONObject(request.body());
    // Jwt AuthEngine = new Jwt();

    // // try to find user
    // String emailOrNull = AuthEngine.verifyJwt((myjson.getString("token")));
    // if (emailOrNull == null) {
    // response.status(404);
    // return false;
    // } else {

    // Document aluno = alunoModel.findByEmail(emailOrNull);

    // if (aluno == null) {
    // response.status(404);
    // return false;
    // }

    // response.status(200);
    // return aluno.toJson();
    // }

    // } catch (JSONException ex) {
    // return false;
    // }
    // }
    // });
    // }
}
