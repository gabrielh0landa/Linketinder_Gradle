import { Candidato, Empresa, Vagas, candidatos, empresas } from './data/index.js';
import { criarFormularioCandidato } from './views/candidatoView.js';
import { criarFormularioEmpresa } from './views/empresaView.js';
import { criarDashboardCandidato } from './views/candidatoDashboardView.js';
import { criarDashboardEmpresa } from './views/empresaDashboardView.js';
import { gerarGraficoSkills } from './views/graficoSkills.js';
import { regexCPF, regexEmail, regexCNPJ, regexCEP, regexNome, regexDescricao } from './regex/regex.js';

type NomePagina = 'cadastro-candidato' | 'cadastro-empresa' | 'dashboard-candidato' | 'dashboard-empresa';

function renderizarTela(nomeDaPagina: NomePagina) {
  const container = document.getElementById('container-principal');
  if (!container) return;

  container.innerHTML = '';

  switch (nomeDaPagina) {
    case 'cadastro-candidato': {
      container.innerHTML = criarFormularioCandidato();
      const form = document.getElementById('formulario-candidato');
      if (form) {
        form.addEventListener('submit', (e) => {
          e.preventDefault();
          const nome = (document.getElementById('nome') as HTMLInputElement).value;
          const email = (document.getElementById('email') as HTMLInputElement).value;
          const cpf = (document.getElementById('cpf') as HTMLInputElement).value;
          const idade = parseInt((document.getElementById('idade') as HTMLInputElement).value);
          const cep = (document.getElementById('cep') as HTMLInputElement).value; // <-- CAPTURA O CEP
          const estado = (document.getElementById('estado') as HTMLInputElement).value; // <-- CAPTURA O ESTADO
          const descricao = (document.getElementById('descricao') as HTMLTextAreaElement).value;
          
          if (!regexNome.test(nome)) { alert('Erro: Por favor, insira um nome válido.'); return; }
          if (!regexEmail.test(email)) { alert('Erro: Por favor, insira um email válido.'); return; }
          if (!regexCPF.test(cpf)) { alert('Erro: Por favor, insira um CPF válido no formato XXX.XXX.XXX-XX.'); return; }
          if (!regexCEP.test(cep)) { alert('Erro: Por favor, insira um CEP válido no formato XXXXX-XXX.'); return; } // <-- VALIDA O CEP
          if (descricao && !regexDescricao.test(descricao)) { alert('Erro: A descrição contém caracteres inválidos.'); return; }

          const skillsSelecionadas: string[] = [];
          document.querySelectorAll('input[name="skills"]:checked').forEach(input => {
            skillsSelecionadas.push((input as HTMLInputElement).value);
          });
          
          const novoCandidato = new Candidato(nome, email, cpf, idade, cep, estado, descricao, skillsSelecionadas);
          candidatos.push(novoCandidato);
          alert('Candidato cadastrado com sucesso!');
          (form as HTMLFormElement).reset();
        });
      }
      break;
    }

    case 'cadastro-empresa': {
      container.innerHTML = criarFormularioEmpresa();
      const vagasTemporarias: Vagas[] = [];
      const btnAdicionarVaga = document.getElementById('btn-adicionar-vaga');
      const listaVagasUl = document.getElementById('lista-vagas-adicionadas');
      
      const form = document.getElementById('formulario-empresa');
      if (form) {
        form.addEventListener('submit', (e) => {
          e.preventDefault();
          const nome = (document.getElementById('nome-empresa') as HTMLInputElement).value;
          const email = (document.getElementById('email-empresa') as HTMLInputElement).value;
          const cnpj = (document.getElementById('cnpj-empresa') as HTMLInputElement).value;
          const cep = (document.getElementById('cep-empresa') as HTMLInputElement).value; // <-- CAPTURA O CEP
          const estado = (document.getElementById('estado-empresa') as HTMLInputElement).value; // <-- CAPTURA O ESTADO
          const descricao = (document.getElementById('descricao-empresa') as HTMLTextAreaElement).value;

          if (!regexEmail.test(email)) { alert('Erro: Por favor, insira um email válido.'); return; }
          if (!regexCNPJ.test(cnpj)) { alert('Erro: Por favor, insira um CNPJ válido no formato XX.XXX.XXX/XXXX-XX.'); return; }
          if (!regexCEP.test(cep)) { alert('Erro: Por favor, insira um CEP válido no formato XXXXX-XXX.'); return; } // <-- VALIDA O CEP
          if (descricao && !regexDescricao.test(descricao)) { alert('Erro: A descrição contém caracteres inválidos.'); return; }

          const novaEmpresa = new Empresa(nome, email, cnpj, cep, estado, vagasTemporarias, descricao);
          empresas.push(novaEmpresa);
          alert('Empresa e suas vagas cadastradas com sucesso!');
          (form as HTMLFormElement).reset();
          if(listaVagasUl) listaVagasUl.innerHTML = '';
        });
      }

      btnAdicionarVaga?.addEventListener('click', () => {
        const nomeVagaInput = document.getElementById('nome-vaga') as HTMLInputElement;
        const nomeVaga = nomeVagaInput.value;
        if (!nomeVaga) { alert('Por favor, digite o nome da vaga.'); return; }
        const skillsVagaSelecionadas: string[] = [];
        document.querySelectorAll('input[name="vaga-skills"]:checked').forEach(input => {
          skillsVagaSelecionadas.push((input as HTMLInputElement).value);
        });
        const novaVaga = new Vagas(nomeVaga, skillsVagaSelecionadas);
        vagasTemporarias.push(novaVaga);
        if(listaVagasUl) {
            const listItem = document.createElement('li');
            listItem.textContent = `${novaVaga.nome} (Skills: ${novaVaga.skills.join(', ') || 'Nenhuma'})`;
            listaVagasUl.appendChild(listItem);
        }
        nomeVagaInput.value = '';
        document.querySelectorAll('input[name="vaga-skills"]:checked').forEach(input => {
          (input as HTMLInputElement).checked = false;
        });
      });
      break;
    }
    
    case 'dashboard-candidato': {
      container.innerHTML = criarDashboardCandidato(empresas);
      break;
    }

    case 'dashboard-empresa': {
      container.innerHTML = criarDashboardEmpresa(candidatos);
      gerarGraficoSkills(candidatos); 
      break;
    }
  }
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('nav-cadastro-candidato')?.addEventListener('click', () => renderizarTela('cadastro-candidato'));
  document.getElementById('nav-cadastro-empresa')?.addEventListener('click', () => renderizarTela('cadastro-empresa'));
  document.getElementById('nav-dashboard-candidato')?.addEventListener('click', () => renderizarTela('dashboard-candidato'));
  document.getElementById('nav-dashboard-empresa')?.addEventListener('click', () => renderizarTela('dashboard-empresa'));

  renderizarTela('cadastro-candidato');
});