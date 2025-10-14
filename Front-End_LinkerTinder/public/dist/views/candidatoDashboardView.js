export function criarDashboardCandidato(empresas) {
    if (empresas.length === 0 || empresas.every(emp => emp.vagas.length === 0)) {
        return '<h2>Nenhuma vaga cadastrada no momento.</h2>';
    }
    const vagasHtml = empresas.map(empresa => empresa.vagas.map(vaga => `
      <div class="vaga-card">
        <h3>${vaga.nome}</h3>
        <p><strong>Empresa:</strong> Confidencial</p>
        <p><strong>Skills necessárias:</strong> ${vaga.skills.join(', ') || 'Não especificado'}</p>
      </div>
    `).join('')).join('');
    return `
    <h2>Vagas Disponíveis</h2>
    <div id="lista-de-vagas">
      ${vagasHtml}
    </div>
  `;
}
