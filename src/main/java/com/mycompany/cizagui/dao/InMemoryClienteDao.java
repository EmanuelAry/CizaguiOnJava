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
    public Map<UUID, IClienteModel> clientes = new LinkedHashMap<>();
            
    @Override
    public void salvar(IClienteModel cliente) throws ClienteException {
        String cpfProcurado = cliente.getCpf().replaceAll("\\D", "");
        boolean duplicado = clientes.values().stream()
                .anyMatch(existente -> !existente.getId().equals(cliente.getId())
                        && existente.getCpf().replaceAll("\\D", "").equals(cpfProcurado));
        if (duplicado) {
            throw new ClienteException("Já existe um cliente na base com o cpf informado");
        }
        clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Optional<IClienteModel> buscarPorCpf(String cpf) {
        String cpfNormalizado = cpf.replaceAll("\\D", "");
        return clientes.values().stream().filter(cliente -> cliente.getCpf().equals(cpfNormalizado)).findFirst();
    }
    @Override
    public Optional<IClienteModel> buscarPorId(UUID id) {
        return Optional.ofNullable(clientes.get(id));
    }

    @Override
    public void excluir(UUID id) {
        clientes.remove(id);
    }

    @Override
    public List<IClienteModel> listarTodos() {
        return List.copyOf(clientes.values());
    }
    
}
