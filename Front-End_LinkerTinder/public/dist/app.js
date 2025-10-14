import { Candidato, Empresa, Vagas, candidatos, empresas } from './data/index.js';
import { criarFormularioCandidato } from './views/candidatoView.js';
import { criarFormularioEmpresa } from './views/empresaView.js';
import { criarDashboardCandidato } from './views/candidatoDashboardView.js';
import { criarDashboardEmpresa } from './views/empresaDashboardView.js';
import { gerarGraficoSkills } from './views/graficoSkills.js';
import { regexCPF, regexEmail, regexCNPJ, regexCEP, regexNome, regexDescricao } from './regex/regex.js';
function renderizarTela(nomeDaPagina) {
    const container = document.getElementById('container-principal');
    if (!container)
        return;
    container.innerHTML = '';
    switch (nomeDaPagina) {
        case 'cadastro-candidato': {
            container.innerHTML = criarFormularioCandidato();
            const form = document.getElementById('formulario-candidato');
            if (form) {
                form.addEventListener('submit', (e) => {
                    e.preventDefault();
                    const nome = document.getElementById('nome').value;
                    const email = document.getElementById('email').value;
                    const cpf = document.getElementById('cpf').value;
                    const idade = parseInt(document.getElementById('idade').value);
                    const cep = document.getElementById('cep').value; // <-- CAPTURA O CEP
                    const estado = document.getElementById('estado').value; // <-- CAPTURA O ESTADO
                    const descricao = document.getElementById('descricao').value;
                    if (!regexNome.test(nome)) {
                        alert('Erro: Por favor, insira um nome válido.');
                        return;
                    }
                    if (!regexEmail.test(email)) {
                        alert('Erro: Por favor, insira um email válido.');
                        return;
                    }
                    if (!regexCPF.test(cpf)) {
                        alert('Erro: Por favor, insira um CPF válido no formato XXX.XXX.XXX-XX.');
                        return;
                    }
                    if (!regexCEP.test(cep)) {
                        alert('Erro: Por favor, insira um CEP válido no formato XXXXX-XXX.');
                        return;
                    } // <-- VALIDA O CEP
                    if (descricao && !regexDescricao.test(descricao)) {
                        alert('Erro: A descrição contém caracteres inválidos.');
                        return;
                    }
                    const skillsSelecionadas = [];
                    document.querySelectorAll('input[name="skills"]:checked').forEach(input => {
                        skillsSelecionadas.push(input.value);
                    });
                    // USA AS VARIÁVEIS, REMOVENDO OS MOCKS
                    const novoCandidato = new Candidato(nome, email, cpf, idade, cep, estado, descricao, skillsSelecionadas);
                    candidatos.push(novoCandidato);
                    alert('Candidato cadastrado com sucesso!');
                    form.reset();
                });
            }
            break;
        }
        case 'cadastro-empresa': {
            container.innerHTML = criarFormularioEmpresa();
            const vagasTemporarias = [];
            const btnAdicionarVaga = document.getElementById('btn-adicionar-vaga');
            const listaVagasUl = document.getElementById('lista-vagas-adicionadas');
            const form = document.getElementById('formulario-empresa');
            if (form) {
                form.addEventListener('submit', (e) => {
                    e.preventDefault();
                    const nome = document.getElementById('nome-empresa').value;
                    const email = document.getElementById('email-empresa').value;
                    const cnpj = document.getElementById('cnpj-empresa').value;
                    const cep = document.getElementById('cep-empresa').value; // <-- CAPTURA O CEP
                    const estado = document.getElementById('estado-empresa').value; // <-- CAPTURA O ESTADO
                    const descricao = document.getElementById('descricao-empresa').value;
                    if (!regexEmail.test(email)) {
                        alert('Erro: Por favor, insira um email válido.');
                        return;
                    }
                    if (!regexCNPJ.test(cnpj)) {
                        alert('Erro: Por favor, insira um CNPJ válido no formato XX.XXX.XXX/XXXX-XX.');
                        return;
                    }
                    if (!regexCEP.test(cep)) {
                        alert('Erro: Por favor, insira um CEP válido no formato XXXXX-XXX.');
                        return;
                    } // <-- VALIDA O CEP
                    if (descricao && !regexDescricao.test(descricao)) {
                        alert('Erro: A descrição contém caracteres inválidos.');
                        return;
                    }
                    const novaEmpresa = new Empresa(nome, email, cnpj, cep, estado, vagasTemporarias, descricao);
                    empresas.push(novaEmpresa);
                    alert('Empresa e suas vagas cadastradas com sucesso!');
                    form.reset();
                    if (listaVagasUl)
                        listaVagasUl.innerHTML = '';
                });
            }
            btnAdicionarVaga === null || btnAdicionarVaga === void 0 ? void 0 : btnAdicionarVaga.addEventListener('click', () => {
                const nomeVagaInput = document.getElementById('nome-vaga');
                const nomeVaga = nomeVagaInput.value;
                if (!nomeVaga) {
                    alert('Por favor, digite o nome da vaga.');
                    return;
                }
                const skillsVagaSelecionadas = [];
                document.querySelectorAll('input[name="vaga-skills"]:checked').forEach(input => {
                    skillsVagaSelecionadas.push(input.value);
                });
                const novaVaga = new Vagas(nomeVaga, skillsVagaSelecionadas);
                vagasTemporarias.push(novaVaga);
                if (listaVagasUl) {
                    const listItem = document.createElement('li');
                    listItem.textContent = `${novaVaga.nome} (Skills: ${novaVaga.skills.join(', ') || 'Nenhuma'})`;
                    listaVagasUl.appendChild(listItem);
                }
                nomeVagaInput.value = '';
                document.querySelectorAll('input[name="vaga-skills"]:checked').forEach(input => {
                    input.checked = false;
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
    var _a, _b, _c, _d;
    (_a = document.getElementById('nav-cadastro-candidato')) === null || _a === void 0 ? void 0 : _a.addEventListener('click', () => renderizarTela('cadastro-candidato'));
    (_b = document.getElementById('nav-cadastro-empresa')) === null || _b === void 0 ? void 0 : _b.addEventListener('click', () => renderizarTela('cadastro-empresa'));
    (_c = document.getElementById('nav-dashboard-candidato')) === null || _c === void 0 ? void 0 : _c.addEventListener('click', () => renderizarTela('dashboard-candidato'));
    (_d = document.getElementById('nav-dashboard-empresa')) === null || _d === void 0 ? void 0 : _d.addEventListener('click', () => renderizarTela('dashboard-empresa'));
    renderizarTela('cadastro-candidato');
});
