export class Vagas {
  constructor(
    public nome: string,
    public skills: string[]
  ) {}
}

export class Candidato {
  constructor(
    public nome: string,
    public email: string,
    public cpf: string,
    public idade: number,
    public cep: string,
    public estado: string,
    public descricao: string,
    public skills: string[] = []
  ) {}
}

export class Empresa {
  constructor(
    public nome: string,
    public email: string,
    public cnpj: string,
    public cep: string,
    public estado: string,
    public vagas: Vagas[] = [],
    public descricao: string
  ) {}
}

export const candidatos: Candidato[] = [];
export const empresas: Empresa[] = [];

export const skillsDisponiveis = ['HTML', 'CSS', 'JavaScript', 'TypeScript', 'React', 'Node.js', 'Python', 'Java'];