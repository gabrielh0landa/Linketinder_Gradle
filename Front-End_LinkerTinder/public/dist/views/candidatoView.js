import { skillsDisponiveis } from '../data/index.js';
export function criarFormularioCandidato() {
    const skillsHtml = skillsDisponiveis.map(skill => `
    <div class="skill-checkbox">
      <input type="checkbox" id="${skill.toLowerCase()}" name="skills" value="${skill}">
      <label for="${skill.toLowerCase()}">${skill}</label>
    </div>
  `).join('');
    return `
    <form id="formulario-candidato">
      <h2>Cadastro de Candidato</h2>
      
      <label for="nome">Nome:</label>
      <input type="text" id="nome" required>
      
      <label for="email">Email:</label>
      <input type="email" id="email" required>

      <label for="cpf">CPF (formato XXX.XXX.XXX-XX):</label>
      <input type="text" id="cpf" required>

      <label for="idade">Idade:</label>
      <input type="number" id="idade" required>

      <label for="cep">CEP (formato XXXXX-XXX):</label>
      <input type="text" id="cep" required>

      <label for="estado">Estado (UF):</label>
      <input type="text" id="estado" required maxlength="2">
      <label for="descricao">Descrição / Breve Resumo:</label>
      <textarea id="descricao" rows="4"></textarea>
      
      <h3>Skills:</h3>
      <div id="skills-container">
        ${skillsHtml}
      </div>
              
      <button type="submit">Cadastrar Candidato</button>
    </form>
  `;
}
