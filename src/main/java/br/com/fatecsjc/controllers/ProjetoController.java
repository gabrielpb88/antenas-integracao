package br.com.fatecsjc.controllers;

import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.utils.TextUtils;
import org.bson.Document;

import static spark.Spark.*;

public class ProjetoController {

    private static ProjetoController controller;
    private static ProjetoService service;

    private ProjetoController() {
        service = new ProjetoService();
    }

    /**
     * Método estático encarregado de criar uma instancia do Controller e iniciar todos os Endpoints /projetos
     */
    public static void init() {
        if (controller == null) {
            controller = new ProjetoController();
        }
        initiateEndpoints();
    }

    /**
     * Inicia todos os endpoints deste Controller
     */
    private static void initiateEndpoints() {

        path("/projetos", () -> {
            get("", (req, res) -> {
                return TextUtils.converter(service.findAll());
            });

            post("", (req, res) -> {
                String jsonString = req.body();
                Document project = Document.parse(jsonString);
                service.update(project);

                return project.toJson();
            });

            put("", (req, res) -> {
                return service.update(Document.parse(req.body())) == null ? "projeto não encontrado"
                        : "projeto deletado";
            });

            delete("", (req, res) -> {
                return service.delete(Document.parse(res.body())).getDeletedCount() > 0;
            });

            get("/empresario", (req, res) -> {
                return TextUtils.converter(service.findByEmpresario(req.queryString()));
            });
        });
    }
}
