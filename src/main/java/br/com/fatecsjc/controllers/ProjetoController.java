package br.com.fatecsjc.controllers;

import br.com.fatecsjc.services.ProjetoService;
import br.com.fatecsjc.utils.TextUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

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
                return service.save(Document.parse(req.body()));
            });

            put("", (req, res) -> {
//                MongoCollection<Document> projetos = db.getCollection("projeto");
//                BasicDBObject query = new BasicDBObject();
//                query.append("_id", projeto.get("_id"));
//                Bson newDocument = new Document("$set", projeto);
//                return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
                return service.update(Document.parse(req.body()));
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
