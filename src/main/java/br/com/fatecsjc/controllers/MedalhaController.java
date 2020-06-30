package br.com.fatecsjc.controllers;

import br.com.fatecsjc.models.dao.MedalhaDao;
import br.com.fatecsjc.models.entities.Medalha;
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

    private MedalhaDao medalhaDao = new MedalhaDao();
    private Gson gson = new Gson();

    public MedalhaController(){}

    public void melhadas() {
        get("/medalhas", (request, response) -> {
            List<String> medalhas = new ArrayList<>();
            medalhaDao.findAll().spliterator().forEachRemaining(medalha -> medalhas.add(gson.toJson(medalha)));
            return medalhas;
        });

        get("/medalhas/:competencia", (request, response) -> {
            List<String> medalhas = new ArrayList<>();
            medalhaDao.findByCompetencia(request.params("competencia")).spliterator().forEachRemaining(medalha -> medalhas.add(gson.toJson(medalha)));
            return medalhas;
        });

        post("/medalhas", (request, response) -> {
            JSONObject body = new JSONObject(request.body());
            String nome = body.get("nome").toString();

            if(medalhaDao.exists(nome)){
                response.status(409);
                return "Medalha já existente";
            }

            // Daqui pra baixo é coisa do Mongo
            Medalha bronze = new Medalha();
            bronze.setCompetencia(nome);
            bronze.setMedalha("Bronze");
            medalhaDao.save(bronze);

            Medalha prata = new Medalha();
            prata.setCompetencia(nome);
            prata.setMedalha("Prata");
            medalhaDao.save(prata);

            Medalha ouro = new Medalha();
            ouro.setCompetencia(nome);
            ouro.setMedalha("Ouro");
            medalhaDao.save(ouro);

            FindIterable<Medalha> iterable = medalhaDao.findByCompetencia(nome);

            return StreamSupport.stream(iterable.spliterator(), false).map(m -> gson.toJson(m))
                    .collect(Collectors.toList());

        });

    }

}
