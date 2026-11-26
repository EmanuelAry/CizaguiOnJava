/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import com.mycompany.cizagui.exeptions.ContaAcessoException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;

/**
 *
 * @author ary
 */
public class InMemoryContaAcessoDao implements IContaAcessoDao{
    private static final Map<String, IContaAcessoModel> contas = new LinkedHashMap<>();

    @Override
    public boolean estaVazio() {
        return contas.isEmpty();
    }

    @Override
    public Optional<IContaAcessoModel> buscarPorCnpjEmail(String cnpj, String email) {
        return Optional.ofNullable(contas.get(chave(cnpj, email)));
    }

    @Override
    public List<IContaAcessoModel> listarPorCnpj(String cnpj) {
        return contas.values().stream()
                .filter(conta -> conta.getCnpj().equals(cnpj))
                .toList();
    }

    @Override
    public void salvar(IContaAcessoModel conta) throws ContaAcessoException {
        String chave = chave(conta.getCnpj(), conta.getEmail());
        IContaAcessoModel existente = contas.get(chave);
        if (existente != null && !existente.getId().equals(conta.getId())) {
            throw new ContaAcessoException(
                    "Já existe uma conta de acesso cadastrada para este e-mail nesta clínica.");
        }
        contas.put(chave, conta);
    }
    
    private String chave(String cnpj, String email) {
        return cnpj + ":" + email;
    }
}
