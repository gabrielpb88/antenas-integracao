package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.entities.Medalha;
import br.com.fatecsjc.services.MedalhaService;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static spark.Spark.get;
import static spark.Spark.post;

public class MedalhaController {

    private MedalhaService medalhaService;
    private Gson gson = new Gson();

    public MedalhaController(){
        medalhaService = new MedalhaService();
    }

    public void melhadas() {
        get("/medalhas", (request, response) -> {
            List<String> medalhas = new ArrayList<>();
            medalhaService.findAll().spliterator().forEachRemaining(medalha -> medalhas.add(gson.toJson(medalha)));
            return medalhas;
        });

        get("/medalhas/:competencia", (request, response) -> {
            List<String> medalhas = new ArrayList<>();
            medalhaService.findByCompetencia(request.params("competencia")).spliterator().forEachRemaining(medalha -> medalhas.add(gson.toJson(medalha)));
            return medalhas;
        });

        post("/medalhas", (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            String nome = body.get("nome").toString();

            // Daqui pra baixo é coisa do Mongo
            Medalha bronze = new Medalha();
            bronze.setCompetencia(nome);
            bronze.setMedalha("Bronze");
            medalhaService.save(bronze);

            Medalha prata = new Medalha();
            prata.setCompetencia(nome);
            prata.setMedalha("Prata");
            medalhaService.save(prata);

            Medalha ouro = new Medalha();
            ouro.setCompetencia(nome);
            ouro.setMedalha("Ouro");
            medalhaService.save(ouro);

            FindIterable<Medalha> iterable = medalhaService.findByCompetencia(nome);

            return StreamSupport.stream(iterable.spliterator(), false).map(m -> gson.toJson(m))
                    .collect(Collectors.toList());

        });

    }

}
