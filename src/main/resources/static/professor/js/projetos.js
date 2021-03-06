/**/
var session_login = sessionStorage.getItem("sess_email_professor");

if (session_login == null) {
  window.location.href = 'index.html';
}

$(document).ready(async function () {
  let projects;

  $.post("/professorLogado", JSON.stringify({ 'email': session_login }), function (user) {
    userData(user);
  }, "json");

  $('[data-save-compentency]').click(e => {
    const inputCompetencia = $("#competencia")
    const competencia = inputCompetencia.val().trim()
    console.log(competencia)
    if(competencia.length > 0){
      $.ajax({ type: 'post', url: '/medalhas', data: JSON.stringify({ 'nome': competencia }) })
          .done(() => {
            location.reload()
          })
          .fail(error => {
            if(error.status === 409){
              alert("Esta medalha já existe!")
            }
          })
    } else {
      inputCompetencia.addClass("alert alert-danger")
      alert("O campo não pode estar vazio!")
    }

  })

  // const aluno = await $.ajax({ type: 'get', url: '/alunos/' + email })

  // $.ajax({ type: 'put', url: '/aluno', data: aluno })

  listarMedalhas()

  $.get('/myprojects', session_login)
    .done(function (projetos) {
      projects = JSON.parse(projetos);
      insertMyProjects(projects);
    });

  function insertMyProjects(projecs) {

    let tbody = $('#tabela-projetos');

    projecs.forEach(project => {
      let project_id = project._id.$oid;
      let tr2 = $.parseHTML(`<tr data-project-item="${project._id}> 
          <th scope="row">${ project.titulo}</th>
              <td>${ project.titulo}</td>
              <td>${ project['descricao-breve']}</td>
              <td>Nome da Empresa</td>
              <td id="td-key">${project['chave'] != null ? project['chave'] : '<input type="text" class="form-control" id="keyAlId-' + project_id + '" name="key_al" placeholder="Inserir Chave">'}<td>
              <td id="td-alkey-${project_id}"></td>
              <td id="td-alunos-${project_id}"></td>
          </tr>
        `);

      tbody.append(tr2);

      /*Gerando Chave de Acesso do Aluno td-alkey */
      let generateKey = $.parseHTML(`<button type="button" class="btn btn-primary">
            Gerar Chave
        </button>
        </li>`);

      let removeKey = $.parseHTML(`<button type="button" class="btn btn-danger">
            Remover Chave
        </button>
        </li>`);

      if (project['chave'] == null) {
        let $generateKey = $(generateKey);
        $generateKey.click(function (e) {
          e.preventDefault();
          var myKey = $('#keyAlId-' + project_id).val();

          if (confirm('Deseja realmente alterar o chave dos alunos ?')) {
            $.post("/updateProjetoProfessor", JSON.stringify({ '_id': project._id, 'chave': myKey }), "json");
            location.reload();
          }
        });

        $('#td-alkey-' + project_id).append(generateKey);
        /* </ >Gerando Chave de Acesso do Aluno td-alkey </ >*/
      }
      else {
        let $removeKey = $(removeKey);
        $removeKey.click(function (e) {
          e.preventDefault();
          var myKey = null;
          if (confirm('Deseja realmente alterar o chave dos alunos ?')) {
            $.post("/updateProjetoProfessor", JSON.stringify({ '_id': project._id, 'chave': myKey }), "json");
            location.reload();
          }
        });

        $('#td-alkey-' + project_id).append(removeKey);

      }

      /*Gerenciar alunos presentes td-alunos*/
      let AlPresentes = $.parseHTML(`
        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#modal-alunos-presentes">
           Alunos Presentes
        </button>`);

      let $AlPresentes = $(AlPresentes);

      $AlPresentes.click(function (e) {
        e.preventDefault();
        _AlunosPresentes(project);
      });

      $('#td-alunos-' + project_id).append(AlPresentes);
      /*</ >Gerenciar alunos presentes td-alunos</ >*/
    });
  }
})


function userData(user) {
  /* <> Logou do Usuário */
  let navPROF = $('[data-user]');
  let logout = $.parseHTML(`
      <li><i class="fa fa-sign-out" aria-hidden="true"></i> 
      <button type="button" class="btn btn-danger">Logout</button></li>`);

  let $logout = $(logout);
  $logout.click(function (e) {
    e.preventDefault();
    if (confirm('Realmente deseja Sair ?')) {
      sessionStorage.clear(session_login);
      window.location.href = 'index.html';
    }
  });

  /* Alterar Senha */
  let updateSenha = $.parseHTML(`
      <li><i class="fa fa-sign-out" aria-hidden="true"></i>
      <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-update-senha">
          Alterar Senha
      </button>
      </li>`);

  let $updateSenha = $(updateSenha);
  $updateSenha.click(function (e) {
    e.preventDefault();
    _formUpdateSenha(user);
  });

  let data = $.parseHTML(`
      <li>${user.nome}</li>`);

  navPROF.append(data);
  navPROF.append(updateSenha);
  navPROF.append(logout);
  $("li").addClass("list-inline-item");
}

function listarMedalhas() {
  $.ajax({ type: 'get', url: '/medalhas' })
    .done(data => {
      const select = $('#medalhas')
      const medalhas = JSON.parse(data)
      medalhas.forEach(medalha => {
        const m = `<option value=${medalha.id._id }>${medalha.competencia} - ${medalha.medalha}</option>`
        select.append(m)
      })
    })
}

function _AlunosPresentes(project) {

  function atribuirMedalha(aluno, medalha){
    const req = {
      email: aluno,
      professor: session_login,
      medalha
    }
    $.ajax({ type: 'post', url: '/aluno/medalha', data: JSON.stringify(req) }).done(() => {
      alert(`Medalha atribuída ao aluno ${aluno}`)
      location.reload();
    })
  }

  const alunos = project.alunos
  let listaDeAlunos = alunos.map((aluno, index) => `<tr aluno>
    <td></td>
    <td>${aluno}</td>
    <td><select medalhas id="select-${index}" class="form-control"><option>Selecione uma medalha</option></select</td>
    <td><button id="atribuir-medalha-${index}" class="btn btn-success">+</button></td></tr>`
  )

  $.ajax({ type: 'get', url: '/medalhas' })
      .done(data => {
        const select = $('[medalhas]')
        const medalhas = JSON.parse(data)
        medalhas.forEach(medalha => {
          const m = `<option value=${JSON.stringify(medalha.id)}>${medalha.competencia} - ${medalha.medalha}</option>`
          select.append(m)
        })
      })


  /* Evento insere modal no HTML */
  const tbody = document.querySelector("#tbody-alunos")
  tbody.innerHTML += listaDeAlunos

  alunos.forEach((aluno, index) => {
    $(`#atribuir-medalha-${index}`).click((e) => {
      const medalha = $(`#select-${index} :selected`).val()
      atribuirMedalha(aluno, medalha)
    })
  })

  /* Evento Remove modal do HTML */
  $('#modal-alunos-presentes').on('hidden.bs.modal', function (e) {
    tbody.innerHTML = ""
  })

  project.alunos.forEach(aluno => {
    let remover = project.alunos.indexOf(aluno)
    let td = $.parseHTML(`<tr data-alunos-item="${aluno}> 
            <th scope="row">${aluno}</th>
                <td>${aluno}</td>
                <td btn-remove-al-${remover}></td>
            </tr>
          `);

    let btn_remove = $.parseHTML(`<button type="button" class="btn btn-danger">Remover</button>`);
    let $btn_remove = $(btn_remove);
    $btn_remove.click(function (e) {
      project.alunos.splice(remover, 1);
      $.post("/updateProjetoProfessor", JSON.stringify({ '_id': project._id, 'alunos': project.alunos }), "json");
      $(this).closest('tr').remove();
    });

    $('#td_body_aluno').append(td);
    $('[btn-remove-al-' + remover + ']').append(btn_remove);
  });
}

function _formUpdateSenha(user) {

  let form_senha = `
    <div class="modal fade" id="modal-update-senha" tabindex="-1" role="dialog" aria-labelledby="modal-update-senha" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Alteração de Senha</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
       Senha: <input class="form-control"  type="password" id="senha-antiga" name="senha-antiga" placeholder="Senha Atual" style="max-width:350px" required>
       Nova Senha: </label><input class="form-control" type="password" id="senha-nova1" name="senha-nova1" placeholder="Nova Senha" style="max-width:350px" required>
       Nova Senha Novamente: </label><input class="form-control" type="password" id="senha-nova2" name="senha-nova2" placeholder="Nova Senha" style="max-width:350px" required>
        </div>
        <div class="modal-footer" >
          <button type="submit" class="btn btn-primary alterarSenha" id="alterarSenha">Salvar mudanças</button>
          <div id="modal-footer-password"></div>
          </div>
      </div>
    </div>
  </div>`;

  /* Evento insere modal no HTML */
  $(document.body).prepend(form_senha);
  /* Evento Remove modal do HTML */
  $('.close').click(function (e) {
    e.preventDefault();
    $("#modal-update-senha").remove();
    $(".modal-backdrop ").remove();
  });
  /* Evento submita a senha nova */
  $('#alterarSenha').click(function (e) {
    e.preventDefault();
    $("#modal-footer-password").html('');
    var senhaAntiga = $("#senha-antiga").val();
    var senha1 = $("#senha-nova1").val();
    var senha2 = $("#senha-nova2").val();


    if (senhaAntiga === user.senha && senhaAntiga != null) {
      if (senha1 === senha2 && senha1 != null && senha2 != null) {
        $.post("/updateProfessor", JSON.stringify({ '_id': user._id, 'senha': senha1 }), "json");
        $('#modal-footer-password').append($.parseHTML(`<div class="alert alert-success" role="alert">
          Senha alterada com sucesso</div>`));
      } else {
        $('#modal-footer-password').append($.parseHTML(`<div class="alert alert-danger" role="alert">
          Senha de nova ou senha de confirmação inválidas ou não correspondentes.</div>`));
      }
    } else {
      $('#modal-footer-password').append($.parseHTML(`<div class="alert alert-danger" role="alert">
          Senha não corresponde com a atual!, por favor insira a senha correta.
      </div>`));
    }
  });
}

