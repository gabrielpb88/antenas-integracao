$(document).ready(function () {

	document.getElementById('cadastro').style.display = 'none';

	let email = sessionStorage.getItem("sess_email_aluno");
	let rota = "/projetos"

	$.get(rota, function (projetosBE, err) {
		
		let projects = JSON.parse(projetosBE);
		let wichParticipate = [];
		for (i = 0; i < projects.length; i++) {
			isParticipate = projects[i].alunos.find(aluno => aluno == email)
			if (isParticipate) {
				wichParticipate.push(i);
			}
		}
		if (wichParticipate) {
			wichParticipate.map((index) => {
				var $tela = document.querySelector('#tpjr'),
					HTMLTemporario = $tela.innerHTML,
					HTMLNovo = "<tr> <th>" + projects[index].chave + "</th>"
						+ "<th>" + projects[index].titulo + "</th>" + "<th>"
						+ projects[index].fase + "</th>"
						+ `<th><button onclick="abrePopupEntregar(event,chave='${projects[index].chave}')">Entregar</button></th>`
						+ "</tr>";
				HTMLTemporario = HTMLTemporario + HTMLNovo;
				$tela.innerHTML = HTMLTemporario;
			});
		}
	});

	$('#botao-add').click(function () {
		let codigoProjeto = $("#codigo-projetoLabel").val();

		function retornaBack (val) {
			if (val=='[]') {
				document.getElementById("erro-add").style.display = "block";
				return false;
			}
			else {
				let email = sessionStorage.getItem("sess_email_aluno");
				let projeto = JSON.parse(val)[0];
				let alunoJaEhParticipante = false;
				alunoJaEhParticipante = projeto.alunos.find(aluno => aluno == email);
				if (!alunoJaEhParticipante) {
					projeto.alunos.push(email);

					$.ajax({ type: 'PUT', url: '/projetos', data: JSON.stringify(projeto)})
						.done((data) => {
							console.log(data);

							if (data != "false") {
								document.getElementById("erro-add-already").style.display = "none";
								document.getElementById("erro-add").style.display = "none";
								window.location.href = 'principal.html';
							}
							else document.getElementById("erro-add").style.display = "block";
						});
				}
				else {
					document.getElementById("erro-add-already").style.display = "block";
				}
			}
		}
		$.get("/searchaluno/" + codigoProjeto, retornaBack);
	});

	function abrePopupLogin(event) {
		event.preventDefault();
		document.getElementById('login').style.display = 'block';
	}

	function fechaPopupLogin(event) {
		event.preventDefault();
		document.getElementById('login').style.display = 'none';
	}

});	