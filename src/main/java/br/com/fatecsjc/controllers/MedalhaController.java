package br.com.fatecsjc.controllers;

import br.com.fatecsjc.services.MedalhaService;
import br.com.fatecsjc.utils.TextUtils;
import org.bson.Document;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Base64;

import static spark.Spark.get;
import static spark.Spark.post;

public class MedalhaController {

    private MedalhaService medalhaService;

    public MedalhaController(){
        medalhaService = new MedalhaService();
    }

    public void melhadas() {
        get("/medalhas", (request, response) -> {
            return TextUtils.converter(medalhaService.findAll());
        });

        get("/medalhas/:competencia", (request, response) -> {
            return medalhaService.findByCompetencia(request.params("competencia"));
        });

        post("/medalhas", (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            String nome = body.get("nome").toString();

            Document bronze = new Document();
            bronze.append("competencia", nome);
            bronze.append("medalha", "Bronze");
            medalhaService.save(bronze);

            Document prata = new Document();
            prata.append("competencia", nome);
            prata.append("medalha", "Prata");
            medalhaService.save(prata);

            Document ouro = new Document();
            ouro.append("competencia", nome);
            ouro.append("medalha", "Ouro");
            medalhaService.save(ouro);

            return TextUtils.converter(medalhaService.findByCompetencia(nome));
        });

    }

}
