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
    public void salvar(IEvolucaoModel evolucao) throws EvolucaoException {
        EvolucaoModel modelo = exigir(evolucao, EvolucaoModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new EvolucaoException("Erro ao salvar a evolução no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public Optional<IEvolucaoModel> buscarEvolucaoPorId(UUID evolucaoId) {
        EvolucaoModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            encontrada = em.find(EvolucaoModel.class, evolucaoId);
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
    public Optional<IEvolucaoModel> buscarEvolucaoPorAtendimento(UUID atendimentoId) {
        EvolucaoModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EvolucaoModel> evolucoes = em
                    .createQuery("from EvolucaoModel e where e.atendimentoId = :atendimentoId"
                            + " order by e.registradaEm asc", EvolucaoModel.class)
                    .setParameter("atendimentoId", atendimentoId)
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
    public List<IEvolucaoModel> listarEvolucoes() {
        List<IEvolucaoModel> evolucoes = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EvolucaoModel> encontradas = em
                    .createQuery("from EvolucaoModel e order by e.registradaEm asc", EvolucaoModel.class)
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
}
