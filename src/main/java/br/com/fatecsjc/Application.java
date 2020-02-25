package br.com.fatecsjc;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import org.bson.Document;

import br.com.fatecsjc.controllers.AlunoController;
import br.com.fatecsjc.controllers.CadiController;
import br.com.fatecsjc.controllers.EmpresarioController;
import br.com.fatecsjc.controllers.ProfessorController;
import br.com.fatecsjc.controllers.ProjetoController;
import br.com.fatecsjc.models.AlunoModel;
import br.com.fatecsjc.models.CadiModel;
import br.com.fatecsjc.models.EmpresarioModel;
import br.com.fatecsjc.models.ProfessorModel;
import br.com.fatecsjc.models.ProjetoModel;

public class Application {
	private static final ProjetoModel projetoModel = new ProjetoModel();
	private static final CadiModel cadiModel = new CadiModel();
	private static final ProfessorModel professorModel = new ProfessorModel();
	private static final AlunoModel alunoModel = new AlunoModel();
	private static final EmpresarioModel empresarioModel = new EmpresarioModel();

	public static void main(String[] args) {

		// Get port config of heroku on environment variable
		ProcessBuilder process = new ProcessBuilder();
		Integer port;
		if (process.environment().get("PORT") != null) {
			port = Integer.parseInt(process.environment().get("PORT"));
		} else {
			port = 8081;
		}
		port(port);

		staticFileLocation("/static");

		initializeControllers();

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
		initializeProjetoController();
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
		controller.projetos();
		controller.atribuirProjeto();
		controller.listCadi();
		controller.listProf();
		controller.inserirReuniao();
		controller.Auth();
		controller.ativarUsuario();
		controller.atualizaCadi();
	}

	/**
	 * Inicializa as rotas do Empresario
	 */
	private static void initializeEmpresarioController() {
		EmpresarioController controller = new EmpresarioController(empresarioModel);
		controller.cadastroEmpresario();
		controller.getEmpresarios();
		controller.loginEmpresario();
		controller.getProjectByEmpresario();
		controller.ativarUsuario();
		controller.Auth();
		controller.IsAuth();
	}

	/**
	 * Inicializa as rotas do Projeto
	 */
	private static void initializeProjetoController() {
		ProjetoController controller = new ProjetoController(projetoModel);
		controller.cadastroProjeto();
		controller.getProjetos();
		controller.deletaProjeto();
		controller.atualizaProjeto();
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
	}

	/**
	 * Inicializa as rotas do Aluno
	 */
	private static void initializeAlunoController() {
		AlunoController controller = new AlunoController(alunoModel);
		controller.cadastroAluno();
		controller.search();
		controller.projetos();
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
	public static void mockProfessores() {
		professorModel.addProfessor(
				Document.parse("{'name':'Giuliano', 'email':'Giuliano@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));
		professorModel.addProfessor(
				Document.parse("{'name':'Sakaue', 'email':'Sakaue@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));
		professorModel.addProfessor(
				Document.parse("{'name':'Nanci', 'email':'Nanci@fatec.sp.gov.br','senha':'1234', 'ativo':true}"));

	}

}
