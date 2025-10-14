import { skillsDisponiveis } from '../data/index.js';
export function criarFormularioEmpresa() {
    const skillsVagaHtml = skillsDisponiveis.map(skill => `
    <div class="skill-checkbox">
      <input type="checkbox" id="vaga-skill-${skill.toLowerCase()}" name="vaga-skills" value="${skill}">
      <label for="vaga-skill-${skill.toLowerCase()}">${skill}</label>
    </div>
  `).join('');
    return `
    <form id="formulario-empresa">
      <h2>Cadastro de Empresa</h2>
      
      <label for="nome-empresa">Nome da Empresa:</label>
      <input type="text" id="nome-empresa" required>
            
      <label for="email-empresa">Email de Contato:</label>
      <input type="email" id="email-empresa" required>

      <label for="cnpj-empresa">CNPJ (formato XX.XXX.XXX/XXXX-XX):</label>
      <input type="text" id="cnpj-empresa" required>

      <label for="cep-empresa">CEP (formato XXXXX-XXX):</label>
      <input type="text" id="cep-empresa" required>

      <label for="estado-empresa">Estado (UF):</label>
      <input type="text" id="estado-empresa" required maxlength="2">
      <label for="descricao-empresa">Descrição da Empresa:</label>
      <textarea id="descricao-empresa" rows="4"></textarea>

      <hr>

      <h3>Cadastro de Vagas</h3>
      <div id="cadastro-vaga-secao">
        <label for="nome-vaga">Nome da Vaga:</label>
        <input type="text" id="nome-vaga">

        <h4>Skills para esta vaga:</h4>
        <div id="vaga-skills-container">
            ${skillsVagaHtml}
        </div>

        <button type="button" id="btn-adicionar-vaga">Adicionar Vaga</button>
      </div>

      <h4>Vagas Adicionadas</h4>
      <ul id="lista-vagas-adicionadas"></ul>
            
      <button type="submit">Cadastrar Empresa com Vagas</button>
    </form>
  `;
}
