package repository

import model.Candidato
import model.Empresa
import model.Vaga

interface IRepository {

    void adicionarCandidato(Candidato c)
    List<Candidato> listarCandidatos()
    Candidato buscarCandidato(int id)
    void atualizarCandidato(int id, Candidato c)

    void adicionarEmpresa(Empresa e)
    List<Empresa> listarEmpresas()
    Empresa buscarEmpresa(int id)
    void atualizarEmpresa(int id, Empresa e)

    void adicionarVaga(Vaga vaga)
    List<Vaga> listarVagasPorEmpresa(int idEmpresa)
    List<Vaga> listarTodasVagas()

}