/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import com.mycompany.cizagui.model.AtendimentoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação JPA (SQLite) de {@link IAtendimentoDao}.
 *
 * @author ary
 */
public class AtendimentoDao extends BaseDAO implements IAtendimentoDao {

    public AtendimentoDao() {
        super();
    }

    @Override
    public void salvar(IAtendimentoModel atendimento) throws AtendimentoException {
        AtendimentoModel modelo = exigir(atendimento, AtendimentoModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            if (modelo.ocupaHorario()) {
                boolean conflito = em
                        .createQuery("from AtendimentoModel", AtendimentoModel.class)
                        .getResultList().stream()
                        .filter(existente -> !existente.getId().equals(modelo.getId()))
                        .filter(existente -> existente.getClienteId().equals(modelo.getClienteId()))
                        .filter(AtendimentoModel::ocupaHorario)
                        .anyMatch(existente -> AtendimentoModel.horariosConflitam(existente, modelo));
                if (conflito) {
                    throw new AtendimentoException("Já existe um atendimento para este cliente em horário conflitante");
                }
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (AtendimentoException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new AtendimentoException("Erro ao salvar o atendimento no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public Optional<IAtendimentoModel> buscarPorId(UUID id) {
        AtendimentoModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            encontrado = em.find(AtendimentoModel.class, id);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IAtendimentoModel>ofNullable(encontrado);
    }

    @Override
    public void atualizar(IAtendimentoModel atendimento) throws AtendimentoException {
        AtendimentoModel modelo = exigir(atendimento, AtendimentoModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            if (em.find(AtendimentoModel.class, modelo.getId()) == null) {
                throw new AtendimentoException("Atendimento não encontrado: " + modelo.getId());
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (AtendimentoException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new AtendimentoException("Erro ao atualizar o atendimento no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public List<IAtendimentoModel> listarTodos() {
        List<IAtendimentoModel> atendimentos = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<AtendimentoModel> encontrados = em
                    .createQuery("from AtendimentoModel", AtendimentoModel.class)
                    .getResultList();
            em.getTransaction().commit();
            atendimentos.addAll(encontrados);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return atendimentos;
    }
}
