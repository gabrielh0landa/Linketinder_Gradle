export class Vagas {
    constructor(nome, skills) {
        this.nome = nome;
        this.skills = skills;
    }
}
export class Candidato {
    constructor(nome, email, cpf, idade, cep, estado, descricao, skills = []) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.idade = idade;
        this.cep = cep;
        this.estado = estado;
        this.descricao = descricao;
        this.skills = skills;
    }
}
export class Empresa {
    constructor(nome, email, cnpj, cep, estado, vagas = [], descricao) {
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.cep = cep;
        this.estado = estado;
        this.vagas = vagas;
        this.descricao = descricao;
    }
}
export const candidatos = [];
export const empresas = [];
export const skillsDisponiveis = ['HTML', 'CSS', 'JavaScript', 'TypeScript', 'React', 'Node.js', 'Python', 'Java'];
