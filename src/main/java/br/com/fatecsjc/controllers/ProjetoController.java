package br.com.fatecsjc.controllers;

import br.com.fatecsjc.services.ProjetoService;
import org.bson.Document;
import org.json.JSONException;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
	public static void init(){
	    if(controller == null){
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
				return StreamSupport.stream(service.findAll().spliterator(), false)
						.map(Document::toJson)
						.collect(Collectors.joining(", ", "[", "]"));
			});

            post("", (req, res) -> {
                try {
                    res.header("Access-Control-Allow-Origin", "*");
                    String jsonString = req.body();

                    Document project = Document.parse(jsonString);
                    service.save(project);

                    return project.toJson();
                } catch (JSONException ex) {
                    return "erro 500 " + ex;
                }
            });

            put("", (req, res) -> {
                try {
                    res.header("Access-Control-Allow-Origin", "*");
                    return service.update(Document.parse(req.body())) == null ? "projeto não encontrado"
                            : "projeto deletado";
                } catch (Exception ex) {
                    throw ex;
                }
            });

            delete("", (req, res) -> {
                try {
                    res.header("Access-Control-Allow-Origin", "*");
                    return service.delete(Document.parse(res.body())).getDeletedCount() > 0;
                } catch (Exception ex) {
                    throw ex;
                }
            });
		});
	}
}
