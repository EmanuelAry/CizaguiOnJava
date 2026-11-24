/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.dao.IProntuarioDao;
import com.mycompany.cizagui.contracts.model.IEvolucaoModel;
import com.mycompany.cizagui.exeptions.EvolucaoException;
import com.mycompany.cizagui.model.EvolucaoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação JPA (SQLite) de {@link IProntuarioDao}.
 *
 * @author ary
 */
public class ProntuarioDao extends BaseDAO implements IProntuarioDao {

    public ProntuarioDao() {
        super();
    }

    @Override
    public void salvar(String entidadeCnpj, IEvolucaoModel evolucao) throws EvolucaoException {
        EvolucaoModel modelo = exigir(evolucao, EvolucaoModel.class);
        exigirEntidade(entidadeCnpj, modelo.getEntidadeCnpj());
        abrirConexao();
        try {
            em.getTransaction().begin();
            EvolucaoModel porId = em.find(EvolucaoModel.class, modelo.getId());
            if (porId != null && !entidadeCnpj.equals(porId.getEntidadeCnpj())) {
                throw new EvolucaoException("A evolução não pertence à entidade autenticada.");
            }
            Long quantidade = em.createQuery(
                            "select count(a) from AtendimentoModel a where a.id = :atendimentoId and a.entidadeCnpj = :entidadeCnpj",
                            Long.class)
                    .setParameter("atendimentoId", modelo.getAtendimentoId())
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getSingleResult();
            if (quantidade == 0L) {
                throw new EvolucaoException("O atendimento não pertence à entidade autenticada.");
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (EvolucaoException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new EvolucaoException("Erro ao salvar a evolução no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public Optional<IEvolucaoModel> buscarEvolucaoPorId(String entidadeCnpj, UUID evolucaoId) {
        EvolucaoModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EvolucaoModel> encontradas = em
                    .createQuery("from EvolucaoModel e where e.id = :evolucaoId and e.entidadeCnpj = :entidadeCnpj", EvolucaoModel.class)
                    .setParameter("evolucaoId", evolucaoId)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            encontrada = encontradas.isEmpty() ? null : encontradas.get(0);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IEvolucaoModel>ofNullable(encontrada);
    }

    @Override
    public Optional<IEvolucaoModel> buscarEvolucaoPorAtendimento(String entidadeCnpj, UUID atendimentoId) {
        EvolucaoModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EvolucaoModel> evolucoes = em
                    .createQuery("from EvolucaoModel e where e.atendimentoId = :atendimentoId and e.entidadeCnpj = :entidadeCnpj"
                            + " order by e.registradaEm asc", EvolucaoModel.class)
                    .setParameter("atendimentoId", atendimentoId)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .setMaxResults(1)
                    .getResultList();
            em.getTransaction().commit();
            encontrada = evolucoes.isEmpty() ? null : evolucoes.get(0);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IEvolucaoModel>ofNullable(encontrada);
    }

    @Override
    public List<IEvolucaoModel> listarEvolucoes(String entidadeCnpj) {
        List<IEvolucaoModel> evolucoes = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EvolucaoModel> encontradas = em
                    .createQuery("from EvolucaoModel e where e.entidadeCnpj = :entidadeCnpj order by e.registradaEm asc", EvolucaoModel.class)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            em.getTransaction().commit();
            evolucoes.addAll(encontradas);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return evolucoes;
    }

    private static void exigirEntidade(String entidadeCnpj, String modeloCnpj) throws EvolucaoException {
        if (entidadeCnpj == null || entidadeCnpj.isBlank() || !entidadeCnpj.equals(modeloCnpj)) {
            throw new EvolucaoException("A evolução não pertence à entidade autenticada.");
        }
    }
}
