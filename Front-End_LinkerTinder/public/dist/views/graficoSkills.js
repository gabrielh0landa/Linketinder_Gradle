export function gerarGraficoSkills(candidatos) {
    const canvasElement = document.getElementById('grafico-skills');
    if (!canvasElement || candidatos.length === 0) {
        return;
    }
    const contagemSkills = {};
    for (const candidato of candidatos) {
        for (const skill of candidato.skills) {
            contagemSkills[skill] = (contagemSkills[skill] || 0) + 1;
        }
    }
    const labels = Object.keys(contagemSkills);
    const data = Object.values(contagemSkills);
    new Chart(canvasElement, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                    label: '# de Candidatos por Skill',
                    data: data,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}
