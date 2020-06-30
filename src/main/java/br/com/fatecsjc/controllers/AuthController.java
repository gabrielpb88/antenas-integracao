package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.dao.UsuarioDao;
import br.com.fatecsjc.models.entities.Usuario;
import br.com.fatecsjc.utils.Jwt;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;

public class AuthController {

    private UsuarioDao usuarioDao;
    private Gson gson;

    public AuthController() {
        this.usuarioDao = new UsuarioDao();
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

                Usuario aluno = usuarioDao.findByEmail(body.getString("email"));

                if(aluno == null){
                    res.status(400);
                    return "Aluno não encontrado";
                }

                if(!aluno.getSenha().equals(body.getString("senha"))){
                    res.status(400);
                    return "Senha incorreta";
                }

                if (aluno.getAtivo()) {
                    res.status(200);
                    return geradorJwt.GenerateJwt(aluno.getEmail());
                }

                res.status(403);
                return null;

            } catch (JSONException ex) {
                return "erro 500 " + ex;
            }
        });
    }

}
