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
    public void salvar(IClienteModel cliente) throws ClienteException {
        ClienteModel modelo = exigir(cliente, ClienteModel.class);
        abrirConexao();
        try {
            em.getTransaction().begin();
            String cpfProcurado = normalizarCpf(modelo.getCpf());
            boolean duplicado = em
                    .createQuery("from ClienteModel", ClienteModel.class)
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
    public Optional<IClienteModel> buscarPorCpf(String cpf) {
        String procurado = normalizarCpf(cpf);
        ClienteModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> clientes = em
                    .createQuery("from ClienteModel", ClienteModel.class)
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
    public Optional<IClienteModel> buscarPorId(UUID id) {
        ClienteModel encontrado = null;
        abrirConexao();
        try {
            em.getTransaction().begin();
            encontrado = em.find(ClienteModel.class, id);
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
    public void excluir(UUID id) throws ClienteException {
        abrirConexao();
        try {
            em.getTransaction().begin();
            ClienteModel cliente = em.find(ClienteModel.class, id);
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
    public List<IClienteModel> listarTodos() {
        List<IClienteModel> clientes = new ArrayList<>();
        abrirConexao();
        try {
            em.getTransaction().begin();
            List<ClienteModel> encontrados = em
                    .createQuery("from ClienteModel", ClienteModel.class)
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
}
