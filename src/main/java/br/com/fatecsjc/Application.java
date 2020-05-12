package br.com.fatecsjc;

import br.com.fatecsjc.config.CorsFilter;
import br.com.fatecsjc.controllers.*;
import br.com.fatecsjc.models.Aluno;
import br.com.fatecsjc.models.Cadi;
import br.com.fatecsjc.models.Professor;
import br.com.fatecsjc.models.Projeto;
import org.bson.Document;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class Application {
	private static final Projeto projetoModel = new Projeto(); // usado apenas para mockar projetos
	private static final Cadi cadiModel = new Cadi();
	private static final Professor professorModel = new Professor();
	private static final Aluno alunoModel = new Aluno();

	public static void main(String[] args) {

		// Get port config of heroku on environment variable
		ProcessBuilder process = new ProcessBuilder();
		int port;
		if (process.environment().get("PORT") != null) {
			port = Integer.parseInt(process.environment().get("PORT"));
		} else {
			port = 8081;
		}
		port(port);

		staticFileLocation("/static");

		initializeControllers();
		CorsFilter.apply();

//		Descomente a linha abaixo para popular o banco com dados mockados
//		mockModels();
	}

	/**
	 * Inicia todos os Controllers
	 */
	private static void initializeControllers() {
		initializeAlunoController();
		initializeEmpresarioController();
		initializeProfessorController();
		initializeCadiController();
		ProjetoController.init();
	}

	/**
	 * Moka dados de: Professores, Usuarios Cadi e Projetos
	 */
	@SuppressWarnings("unused")
	private static void mockModels() {
		mockProjetos();
		mockCadis();
		mockProfessores();
	}

	/**
	 * Inicializa as rotas do Cadi
	 */
	private static void initializeCadiController() {
		CadiController controller = new CadiController(cadiModel);
		controller.inserirCADI();
		controller.search();
		controller.loginCadi();
		controller.atribuirProjeto();
		controller.listCadi();
		controller.inserirReuniao();
		controller.Auth();
		controller.ativarUsuario();
		controller.atualizaCadi();
	}

	/**
	 * Inicializa as rotas do Empresario
	 */
	private static void initializeEmpresarioController() {
		EmpresarioController controller = new EmpresarioController();
		controller.cadastroEmpresario();
		controller.ativarUsuario();
	}

	/**
	 * Inicializa as rotas do Professor
	 */
	private static void initializeProfessorController() {
		ProfessorController controller = new ProfessorController(professorModel);
		controller.Auth();
		controller.ativarUsuario();
		controller.loginProfessor();
		controller.updateProjetoProfessor();
		controller.searchprofessor();
		controller.atualizaProfessor();
		controller.inserirProfessor();
		controller.initiate();
	}

	/**
	 * Inicializa as rotas do Aluno
	 */
	private static void initializeAlunoController() {
		AlunoController controller = new AlunoController(alunoModel);
		controller.cadastroAluno();
		controller.search();
		controller.atribuirProjeto();
		controller.entregaProjeto();
		controller.ativarUsuario();

		// validacao alunos
		controller.validaAluno();
		controller.loginAluno();
	}

	/**
	 * Moka Projetos
	 */
	private static void mockProjetos() {
		projetoModel.save(Document.parse(
				"{\"_id\": \"1o23u1io2jdpasd\",\"titulo\": \"Um projeto na fase 2\",\"descricao-breve\": \"Nesta fase o usuário tem que esperar uma avaliação detalhada\",\"descricao-completa\": \"\",\"descricao-tecnologias\": \"\",\"link-externo-1\": \"\",\"link-externo-2\": \"\",\"fase\": 2,\"reuniao\": {  \"data\": \"\",  \"horario\": \"\",  local: \"\",  \"datas-possiveis\": []},\"status\": {  \"negado\": false,  \"motivo\": \"\"},\"entregas\": [],\"alunos\": [],\"responsavel-cadi\": \"\",\"responsavel-professor\": [],\"responsavel-empresario\": \"flromeiroc@gmail.com\"}"));
		projetoModel.save(Document.parse(
				"{\"_id\": \"pjpinih321djs\",\"titulo\": \"Um projeto na fase 3\",\"descricao-breve\": \"Nesta fase o usuário tem que esperar uma avaliação detalhada\",\"descricao-completa\": \"Descricao completa lindissima\",\"descricao-tecnologias\": \"Tem até descricao de tecnologia\",\"link-externo-1\": \"http://www.fabioromeiro.com.br\",\"link-externo-2\": \"\",\"fase\": 3,\"reuniao\": {  \"data\": \"\",  \"horario\": \"\",  local: \"\",  \"datas-possiveis\": []},\"status\": {  \"negado\": false,  \"motivo\": \"\"},\"entregas\": [],\"alunos\": [],\"responsavel-cadi\": \"\",\"responsavel-professor\": [],\"responsavel-empresario\": \"flromeiroc@gmail.com\"}"));
		projetoModel.save(Document.parse(
				"{\"_id\": \"ioeoqromksc812\",\"titulo\": \"Um projeto na fase 4\",\"descricao-breve\": \"Nesta fase o usuário tem que esperar uma avaliação detalhada\",\"descricao-completa\": \"Descricao completa lindissima\",\"descricao-tecnologias\": \"Tem até descricao de tecnologia\",\"link-externo-1\": \"http://www.fabioromeiro.com.br\",\"link-externo-2\": \"\",\"fase\": 4,\"reuniao\": {  \"data\": \"\",  \"horario\": \"\",  local: \"\",  \"datas-possiveis\": [    {      \"data\": \"05/04/2019\",      \"hora\": \"15:50\"    },    {      \"data\": \"24/04/2019\",      \"hora\": \"10:20\"    },    {      \"data\": \"09/05/2019\",      \"hora\": \"13:15\"    }  ]},\"status\": {  \"negado\": false,  \"motivo\": \"\"},\"entregas\": [],\"alunos\": [],\"responsavel-cadi\": \"\",\"responsavel-professor\": [],\"responsavel-empresario\": \"flromeiroc@gmail.com\"}"));
		projetoModel.save(Document.parse(
				"{\"_id\": \"qowiu3oiqew521\",\"titulo\": \"Um projeto na fase 5 pendente\",\"descricao-breve\": \"Nesta fase o usuário tem que esperar uma avaliação detalhada\",\"descricao-completa\": \"Descricao completa lindissima\",\"descricao-tecnologias\": \"Tem até descricao de tecnologia\",\"link-externo-1\": \"http://www.fabioromeiro.com.br\",\"link-externo-2\": \"\",\"fase\": 5,\"reuniao\": {  \"data\": \"05/04/2019\",  \"horario\": \"15:50\",  local: \"Rua Barbosa\",  \"datas-possiveis\": [    {      \"data\": \"05/04/2019\",      \"hora\": \"15:50\"    },    {      \"data\": \"24/04/2019\",      \"hora\": \"10:20\"    },    {      \"data\": \"09/05/2019\",      \"hora\": \"13:15\"    }  ]},\"status\": {  \"negado\": false,  \"motivo\": \"\"},\"entregas\": [],\"alunos\": [],\"responsavel-cadi\": \"\",\"responsavel-professor\": [],\"responsavel-empresario\": \"flromeiroc@gmail.com\"}"));
		projetoModel.save(Document.parse(
				"{\"_id\": \"rotejhroncasd51\",\"titulo\": \"Um projeto na fase 5\",\"descricao-breve\": \"Nesta fase o usuário tem que esperar uma avaliação detalhada\",\"descricao-completa\": \"Descricao completa lindissima\",\"descricao-tecnologias\": \"Tem até descricao de tecnologia\",\"link-externo-1\": \"http://www.fabioromeiro.com.br\",\"link-externo-2\": \"\",\"fase\": 5,\"reuniao\": {  \"data\": \"05/04/2019\",  \"horario\": \"15:50\",  local: \"Rua Barbosa\",  \"datas-possiveis\": [    {      \"data\": \"05/04/2019\",      \"hora\": \"15:50\"    },    {      \"data\": \"24/04/2019\",      \"hora\": \"10:20\"    },    {      \"data\": \"09/05/2019\",      \"hora\": \"13:15\"    }  ]},\"status\": {  \"negado\": false,  \"motivo\": \"\"},\"entregas\": [  {    \"aluno-responsavel\": \"flromeiroc@gmail.com\",    \"alunos\": [      \"outros@email.com\"    ],    \"link-repositorio\": \"https://github.com/projeto-antena/antena-empresario\",    \"link-cloud\": \"http://github.com/FabioRomeiro\",    \"comentario\": \"Nos esforçamos bastante nesse projeto mesmo tendo zilhões de provas pra fazer :D\"  }],\"alunos\": [],\"responsavel-cadi\": \"\",\"responsavel-professor\": [],\"responsavel-empresario\": \"flromeiroc@gmail.com\"}"));
	}

	/**
	 * Moka usuarios Cadi
	 */
	private static void mockCadis() {
		cadiModel.addCADI(
				Document.parse("{'email':'rone@email.com','name':'John', 'senha':'11111', 'nivel':'1', 'ativo':true}"));
		cadiModel.addCADI(Document.parse(
				"{'email':'cadi.admin@fatec.sp.gov.br','name':'Administrador', 'senha':'1234', 'nivel':'2', 'ativo':true}"));
		cadiModel.addCADI(Document
				.parse("{'email':'teste@email.com','name':'Francisco', 'senha':'000', 'nivel':'0', 'ativo':true}"));
	}

	/**
	 * Moka Professores
	 */
	private static void mockProfessores() {
		professorModel.addProfessor(
				Document.parse("{'name':'Giuliano', 'email':'Giuliano@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));
		professorModel.addProfessor(
				Document.parse("{'name':'Sakaue', 'email':'Sakaue@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));
		professorModel.addProfessor(
				Document.parse("{'name':'Nanci', 'email':'Nanci@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));

	}

}
