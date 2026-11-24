/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.ClienteException;
import com.mycompany.cizagui.model.ClienteModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação JPA (SQLite) de {@link IClienteDao}.
 *
 * @author ary
 */
public class ClienteDao extends BaseDAO implements IClienteDao {

    public ClienteDao() {
        super();
    }

    @Override
    public void salvar(String entidadeCnpj, IClienteModel cliente) throws ClienteException {
        ClienteModel modelo = exigir(cliente, ClienteModel.class);
        exigirEntidade(entidadeCnpj, modelo.getEntidadeCnpj());
        abrirConexao();
        try {
            em.getTransaction().begin();
            ClienteModel porId = em.find(ClienteModel.class, modelo.getId());
            if (porId != null && !entidadeCnpj.equals(porId.getEntidadeCnpj())) {
                throw new ClienteException("O cliente não pertence à entidade autenticada.");
            }
            String cpfProcurado = normalizarCpf(modelo.getCpf());
            boolean duplicado = em
                    .createQuery("from ClienteModel c where c.entidadeCnpj = :entidadeCnpj", ClienteModel.class)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList().stream()
                    .anyMatch(existente -> !existente.getId().equals(modelo.getId())
                            && normalizarCpf(existente.getCpf()).equals(cpfProcurado));
            if (duplicado) {
                throw new ClienteException("Já existe um cliente na base com o cpf informado");
            }
            em.merge(modelo);
            em.getTransaction().commit();
        } catch (ClienteException e) {
            desfazer();
            throw e;
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new ClienteException("Erro ao salvar o cliente no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public Optional<IClienteModel> buscarPorCpf(String entidadeCnpj, String cpf) {
        String procurado = normalizarCpf(cpf);
        ClienteModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> clientes = em
                    .createQuery("from ClienteModel c where c.entidadeCnpj = :entidadeCnpj", ClienteModel.class)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            em.getTransaction().commit();
            for (ClienteModel cliente : clientes) {
                if (normalizarCpf(cliente.getCpf()).equals(procurado)) {
                    encontrado = cliente;
                    break;
                }
            }
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return Optional.<IClienteModel>ofNullable(encontrado);
    }

    @Override
    public Optional<IClienteModel> buscarPorId(String entidadeCnpj, UUID id) {
        ClienteModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> encontrados = em
                    .createQuery("from ClienteModel c where c.id = :id and c.entidadeCnpj = :entidadeCnpj", ClienteModel.class)
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
        return Optional.<IClienteModel>ofNullable(encontrado);
    }

    @Override
    public void excluir(String entidadeCnpj, UUID id) throws ClienteException {
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> encontrados = em
                    .createQuery("from ClienteModel c where c.id = :id and c.entidadeCnpj = :entidadeCnpj", ClienteModel.class)
                    .setParameter("id", id)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            ClienteModel cliente = encontrados.isEmpty() ? null : encontrados.get(0);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw new ClienteException("Erro ao excluir o cliente no banco de dados.");
        } finally {
            fecharConexao();
        }
    }

    @Override
    public List<IClienteModel> listarTodos(String entidadeCnpj) {
        List<IClienteModel> clientes = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> encontrados = em
                    .createQuery("from ClienteModel c where c.entidadeCnpj = :entidadeCnpj", ClienteModel.class)
                    .setParameter("entidadeCnpj", entidadeCnpj)
                    .getResultList();
            em.getTransaction().commit();
            clientes.addAll(encontrados);
        } catch (RuntimeException e) {
            desfazer();
            e.printStackTrace();
            throw e;
        } finally {
            fecharConexao();
        }
        return clientes;
    }

    private static String normalizarCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    private static void exigirEntidade(String entidadeCnpj, String modeloCnpj) throws ClienteException {
        if (entidadeCnpj == null || entidadeCnpj.isBlank() || !entidadeCnpj.equals(modeloCnpj)) {
            throw new ClienteException("O cliente não pertence à entidade autenticada.");
        }
    }
}
