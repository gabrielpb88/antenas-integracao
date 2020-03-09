package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.Empresario;
import br.com.fatecsjc.services.EmpresarioService;
import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.services.exceptions.AccessForbiddenException;
import br.com.fatecsjc.services.exceptions.UserAlreadyExists;
import br.com.fatecsjc.utils.Jwt;
import br.com.fatecsjc.utils.TextUtils;
import org.bson.Document;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import java.util.Base64;

import static spark.Spark.*;

public class EmpresarioController {

    private EmpresarioService service;

    public EmpresarioController() {
        this.service = new EmpresarioService();
    }

    /**
     * Cadastra um novo usuario
     */
    public void cadastroEmpresario() {

        path("/empresarios", () -> {

            get("", (req, res) -> {
                return TextUtils.converter(service.findAll());
            });

            post("", (req, res) -> {
                Document empresario = Document.parse(req.body());
                try {
                    return service.save(empresario).toJson();
                } catch (UserAlreadyExists e) {
                    res.status(HttpStatus.CONFLICT_409);
                    return e.getMessage();
                }
            });

            post("/login", (req, res) -> {
                Document dadosLogin = Document.parse(req.body());
                try {
                    res.status(200);
                    return service.login(dadosLogin);
                } catch (AccessForbiddenException e) {
                    res.status(403);
                    return e.getMessage();
                }
            });

            post("/is-auth", (req, res) -> {
                try {
                    String token = new JSONObject(req.body()).getString("token");
                    String email = new Jwt().verifyJwt(token);
                    Document empresario = service.findByEmail(email);
                    if (empresario == null) {
                        res.status(404);
                        return false;
                    }
                    res.status(200);
                    return empresario.toJson();
                } catch (Exception ex) {
                    res.status(500);
                    return false;
                }
            });
        });
    }

    /**
     * Altera o status do Empresario para Ativo
     */
    public void ativarUsuario() {
        get("/active/empresario/:email", (req, res) -> {
            String email = new String(Base64.getDecoder().decode(req.params("email")));
            Document empresario = service.findByEmail(email);
            empresario.replace("ativo", true);
            service.update(empresario);
            if (!empresario.isEmpty()) {
                res.redirect("http://localhost:8081/");
            }
            return null;
        });
    }
}
