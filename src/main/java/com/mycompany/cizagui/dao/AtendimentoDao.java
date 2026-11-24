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
    public void salvar(String entidadeCnpj, IAtendimentoModel atendimento) throws AtendimentoException {
        AtendimentoModel modelo = exigir(atendimento, AtendimentoModel.class);
        exigirEntidade(entidadeCnpj, modelo.getEntidadeCnpj());
        abrirConexao();
        try {
            em.getTransaction().begin();
            AtendimentoModel porId = em.find(AtendimentoModel.class, modelo.getId());
            if (porId != null && !entidadeCnpj.equals(porId.getEntidadeCnpj())) {
                throw new AtendimentoException("O atendimento não pertence à entidade autenticada.");
            }
            exigirClienteDaEntidade(entidadeCnpj, modelo.getClienteId());
            if (modelo.ocupaHorario()) {
                boolean conflito = em
                        .createQuery("from AtendimentoModel a where a.entidadeCnpj = :entidadeCnpj", AtendimentoModel.class)
                        .setParameter("entidadeCnpj", entidadeCnpj)
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
    public Optional<IAtendimentoModel> buscarPorId(String entidadeCnpj, UUID id) {
        AtendimentoModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<AtendimentoModel> encontrados = em
                    .createQuery("from AtendimentoModel a where a.id = :id and a.entidadeCnpj = :entidadeCnpj", AtendimentoModel.class)
                    .setParameter("id", id)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            encontrado = encontrados.isEmpty() ? null : encontrados.get(0);
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
    public void atualizar(String entidadeCnpj, IAtendimentoModel atendimento) throws AtendimentoException {
        AtendimentoModel modelo = exigir(atendimento, AtendimentoModel.class);
        exigirEntidade(entidadeCnpj, modelo.getEntidadeCnpj());
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<AtendimentoModel> existentes = em
                    .createQuery("from AtendimentoModel a where a.id = :id and a.entidadeCnpj = :entidadeCnpj", AtendimentoModel.class)
                    .setParameter("id", modelo.getId())
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            if (existentes.isEmpty()) {
                throw new AtendimentoException("Atendimento não encontrado: " + modelo.getId());
            }
            exigirClienteDaEntidade(entidadeCnpj, modelo.getClienteId());
            if (modelo.ocupaHorario()) {
                boolean conflito = em
                        .createQuery("from AtendimentoModel a where a.entidadeCnpj = :entidadeCnpj", AtendimentoModel.class)
                        .setParameter("entidadeCnpj", entidadeCnpj)
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
            throw new AtendimentoException("Erro ao atualizar o atendimento no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public List<IAtendimentoModel> listarTodos(String entidadeCnpj) {
        List<IAtendimentoModel> atendimentos = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<AtendimentoModel> encontrados = em
                    .createQuery("from AtendimentoModel a where a.entidadeCnpj = :entidadeCnpj", AtendimentoModel.class)
                    .setParameter("entidadeCnpj", entidadeCnpj)
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

    private void exigirClienteDaEntidade(String entidadeCnpj, UUID clienteId) throws AtendimentoException {
        Long quantidade = em.createQuery(
                        "select count(c) from ClienteModel c where c.id = :clienteId and c.entidadeCnpj = :entidadeCnpj",
                        Long.class)
                .setParameter("clienteId", clienteId)
                .setParameter("entidadeCnpj", entidadeCnpj)
                .getSingleResult();
        if (quantidade == 0L) {
            throw new AtendimentoException("O cliente não pertence à entidade autenticada.");
        }
    }

    private static void exigirEntidade(String entidadeCnpj, String modeloCnpj) throws AtendimentoException {
        if (entidadeCnpj == null || entidadeCnpj.isBlank() || !entidadeCnpj.equals(modeloCnpj)) {
            throw new AtendimentoException("O atendimento não pertence à entidade autenticada.");
        }
    }
}
