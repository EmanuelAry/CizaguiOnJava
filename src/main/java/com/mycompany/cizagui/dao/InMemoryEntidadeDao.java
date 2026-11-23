/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.model.IEntidadeModel;
import com.mycompany.cizagui.exeptions.EntidadeException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.mycompany.cizagui.contracts.dao.IEntidadeDao;

/**
 *
 * @author ary
 */
public class InMemoryEntidadeDao implements IEntidadeDao{
    
    private final Map<String, IEntidadeModel> entidadesPorCnpj = new LinkedHashMap<>();
    
    @Override
    public boolean estaVazio() {
        return entidadesPorCnpj.isEmpty();
    }

    @Override
    public Optional<IEntidadeModel> buscarPorCnpj(String cnpj) {
        return Optional.ofNullable(entidadesPorCnpj.get(cnpj));
    }

    @Override
    public Optional<IEntidadeModel> buscarPorId(UUID id) {
        return entidadesPorCnpj.values().stream().filter(entidade -> entidade.getId().equals(id)).findFirst();
    }

    @Override
    public List<IEntidadeModel> listarTodas() {
        return List.copyOf(entidadesPorCnpj.values());
    }

    @Override
    public void salvar(IEntidadeModel entidade) throws EntidadeException {
        if (entidadesPorCnpj.containsKey(entidade.getCnpj())) {
            throw new EntidadeException("Já existe uma Entidade cadastrada com este CNPJ");
        }
        entidadesPorCnpj.put(entidade.getCnpj(), entidade);
    }
}
