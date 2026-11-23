/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.dao.IEntidadeDao;
import com.mycompany.cizagui.contracts.model.IEntidadeModel;
import com.mycompany.cizagui.exeptions.EntidadeException;
import com.mycompany.cizagui.model.EntidadeModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação JPA (SQLite) de {@link IEntidadeDao}.
 *
 * @author ary
 */
public class EntidadeDao extends BaseDAO implements IEntidadeDao {

    public EntidadeDao() {
        super();
    }

    @Override
    public boolean estaVazio() {
        boolean vazio = true;
        abrirConexao();
        try {
            em.getTransaction().begin();
            Long total = em
                    .createQuery("select count(e) from EntidadeModel e", Long.class)
                    .getSingleResult();
            em.getTransaction().commit();
            vazio = total == null || total == 0L;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return vazio;
    }

    @Override
    public Optional<IEntidadeModel> buscarPorCnpj(String cnpj) {
        EntidadeModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EntidadeModel> entidades = em
                    .createQuery("from EntidadeModel e where e.cnpj = :cnpj", EntidadeModel.class)
                    .setParameter("cnpj", cnpj)
                    .getResultList();
            em.getTransaction().commit();
            encontrada = entidades.isEmpty() ? null : entidades.get(0);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IEntidadeModel>ofNullable(encontrada);
    }

    @Override
    public Optional<IEntidadeModel> buscarPorId(UUID id) {
        EntidadeModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            encontrada = em.find(EntidadeModel.class, id);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IEntidadeModel>ofNullable(encontrada);
    }

    @Override
    public List<IEntidadeModel> listarTodas() {
        List<IEntidadeModel> entidades = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EntidadeModel> encontradas = em
                    .createQuery("from EntidadeModel", EntidadeModel.class)
                    .getResultList();
            em.getTransaction().commit();
            entidades.addAll(encontradas);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return entidades;
    }

    @Override
    public void salvar(IEntidadeModel entidade) throws EntidadeException {
        EntidadeModel modelo = exigir(entidade, EntidadeModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<EntidadeModel> existentes = em
                    .createQuery("from EntidadeModel e where e.cnpj = :cnpj", EntidadeModel.class)
                    .setParameter("cnpj", modelo.getCnpj())
                    .getResultList();
            boolean mesmaEntidade = existentes.stream()
                    .allMatch(existente -> existente.getId().equals(modelo.getId()));
            if (!existentes.isEmpty() && !mesmaEntidade) {
                throw new EntidadeException("Já existe uma Entidade cadastrada com este CNPJ");
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (EntidadeException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new EntidadeException("Erro ao salvar a clínica no banco de dados.");
        } finally {
            fecharConexao();
        }
    }
}
