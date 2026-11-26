/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.ClienteException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.mycompany.cizagui.contracts.dao.IClienteDao;

/**
 *
 * @author ary
 */
public class InMemoryClienteDao implements IClienteDao {
    public static final Map<UUID, IClienteModel> clientes = new LinkedHashMap<>();
            
    @Override
    public void salvar(String entidadeCnpj, IClienteModel cliente) throws ClienteException {
        exigirEntidade(entidadeCnpj, cliente.getEntidadeCnpj());
        IClienteModel porId = clientes.get(cliente.getId());
        if (porId != null && !entidadeCnpj.equals(porId.getEntidadeCnpj())) {
            throw new ClienteException("O cliente não pertence à entidade autenticada.");
        }
        String cpfProcurado = cliente.getCpf().replaceAll("\\D", "");
        boolean duplicado = clientes.values().stream()
                .anyMatch(existente -> !existente.getId().equals(cliente.getId())
                        && entidadeCnpj.equals(existente.getEntidadeCnpj())
                        && existente.getCpf().replaceAll("\\D", "").equals(cpfProcurado));
        if (duplicado) {
            throw new ClienteException("Já existe um cliente na base com o cpf informado");
        }
        clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Optional<IClienteModel> buscarPorCpf(String entidadeCnpj, String cpf) {
        String cpfNormalizado = cpf.replaceAll("\\D", "");
        return clientes.values().stream()
                .filter(cliente -> entidadeCnpj.equals(cliente.getEntidadeCnpj()))
                .filter(cliente -> cliente.getCpf().replaceAll("\\D", "").equals(cpfNormalizado))
                .findFirst();
    }
    @Override
    public Optional<IClienteModel> buscarPorId(String entidadeCnpj, UUID id) {
        return Optional.ofNullable(clientes.get(id))
                .filter(cliente -> entidadeCnpj.equals(cliente.getEntidadeCnpj()));
    }

    @Override
    public void excluir(String entidadeCnpj, UUID id) {
        buscarPorId(entidadeCnpj, id).ifPresent(cliente -> clientes.remove(id));
    }

    @Override
    public List<IClienteModel> listarTodos(String entidadeCnpj) {
        return clientes.values().stream()
                .filter(cliente -> entidadeCnpj.equals(cliente.getEntidadeCnpj()))
                .toList();
    }

    private static void exigirEntidade(String entidadeCnpj, String modeloCnpj) throws ClienteException {
        if (entidadeCnpj == null || entidadeCnpj.isBlank() || !entidadeCnpj.equals(modeloCnpj)) {
            throw new ClienteException("O cliente não pertence à entidade autenticada.");
        }
    }
    
}
