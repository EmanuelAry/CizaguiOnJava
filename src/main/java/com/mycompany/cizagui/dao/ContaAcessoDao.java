/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;
import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import com.mycompany.cizagui.exeptions.ContaAcessoException;
import com.mycompany.cizagui.model.ContaAcessoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação JPA (SQLite) de {@link IContaAcessoDao}.
 *
 * @author ary
 */
public class ContaAcessoDao extends BaseDAO implements IContaAcessoDao {

    public ContaAcessoDao() {
        super();
    }

    @Override
    public boolean estaVazio() {
        boolean vazio = true;
        abrirConexao();
        try {
            em.getTransaction().begin();
            Long total = em
                    .createQuery("select count(c) from ContaAcessoModel c", Long.class)
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
    public Optional<IContaAcessoModel> buscarPorCnpjEmail(String cnpj, String email) {
        ContaAcessoModel encontrada = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ContaAcessoModel> contas = em
                    .createQuery("from ContaAcessoModel c where c.cnpj = :cnpj and c.email = :email",
                            ContaAcessoModel.class)
                    .setParameter("cnpj", cnpj)
                    .setParameter("email", email)
                    .getResultList();
            em.getTransaction().commit();
            encontrada = contas.isEmpty() ? null : contas.get(0);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IContaAcessoModel>ofNullable(encontrada);
    }

    @Override
    public List<IContaAcessoModel> listarPorCnpj(String cnpj) {
        List<IContaAcessoModel> contas = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ContaAcessoModel> encontradas = em
                    .createQuery("from ContaAcessoModel c where c.cnpj = :cnpj", ContaAcessoModel.class)
                    .setParameter("cnpj", cnpj)
                    .getResultList();
            em.getTransaction().commit();
            contas.addAll(encontradas);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return contas;
    }

    @Override
    public void salvar(IContaAcessoModel conta) throws ContaAcessoException {
        ContaAcessoModel modelo = exigir(conta, ContaAcessoModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ContaAcessoModel> existentes = em
                    .createQuery("from ContaAcessoModel c where c.cnpj = :cnpj and c.email = :email",
                            ContaAcessoModel.class)
                    .setParameter("cnpj", modelo.getCnpj())
                    .setParameter("email", modelo.getEmail())
                    .getResultList();
            boolean mesmaConta = existentes.stream()
                    .allMatch(existente -> existente.getId().equals(modelo.getId()));
            if (!existentes.isEmpty() && !mesmaConta) {
                throw new ContaAcessoException(
                        "Já existe uma conta de acesso cadastrada para este e-mail nesta clínica.");
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (ContaAcessoException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new ContaAcessoException("Erro ao salvar a conta de acesso no banco de dados.");
        } finally {
            fecharConexao();
        }
    }
}
