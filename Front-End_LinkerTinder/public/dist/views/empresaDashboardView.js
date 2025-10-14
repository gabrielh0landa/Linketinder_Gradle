export function criarDashboardEmpresa(candidatos) {
    if (candidatos.length === 0) {
        return '<h2>Nenhum candidato cadastrado no momento.</h2>';
    }
    const candidatosHtml = candidatos.map((candidato, index) => `
    <div class="candidato-card">
      <h3>Candidato #${index + 1}</h3>
      <p><strong>Idade:</strong> ${candidato.idade} anos</p>
      <p><strong>Skills:</strong> ${candidato.skills.join(', ') || 'Nenhuma skill cadastrada'}</p>
      <p><strong>Descrição:</strong> ${candidato.descricao || 'Nenhuma descrição.'}</p>
    </div>
  `).join('');
    return `
    <h2>Gráfico de Competências</h2>
    <canvas id="grafico-skills"></canvas>

    <hr>

    <h2>Candidatos Disponíveis (Anônimos)</h2>
    <div id="lista-de-candidatos">
      ${candidatosHtml}
    </div>
  `;
}
