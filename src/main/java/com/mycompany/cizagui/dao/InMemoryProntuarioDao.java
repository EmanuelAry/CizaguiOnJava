/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.model.IEvolucaoModel;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.mycompany.cizagui.contracts.dao.IProntuarioDao;

/**
 *
 * @author ary
 */
public class InMemoryProntuarioDao implements IProntuarioDao{

    private final Map<UUID, IEvolucaoModel> evolucoes = new LinkedHashMap<>();

    @Override
    public void salvar(String entidadeCnpj, IEvolucaoModel evolucao) {
        exigirEntidade(entidadeCnpj, evolucao.getEntidadeCnpj());
        evolucoes.put(evolucao.getId(), evolucao);
    }

    @Override
    public Optional<IEvolucaoModel> buscarEvolucaoPorId(String entidadeCnpj, UUID evolucaoId) {
        return Optional.ofNullable(evolucoes.get(evolucaoId))
                .filter(evolucao -> entidadeCnpj.equals(evolucao.getEntidadeCnpj()));
    }

    @Override
    public Optional<IEvolucaoModel> buscarEvolucaoPorAtendimento(String entidadeCnpj, UUID atendimentoId) {
        return evolucoes.values().stream()
                .filter(evolucao -> entidadeCnpj.equals(evolucao.getEntidadeCnpj()))
                .filter(evolucao -> evolucao.getAtendimentoId().equals(atendimentoId))
                .findFirst();
    }

    @Override
    public List<IEvolucaoModel> listarEvolucoes(String entidadeCnpj) {
        return evolucoes.values().stream()
                .filter(evolucao -> entidadeCnpj.equals(evolucao.getEntidadeCnpj()))
                .toList();
    }

    private static void exigirEntidade(String entidadeCnpj, String modeloCnpj) {
        if (entidadeCnpj == null || entidadeCnpj.isBlank() || !entidadeCnpj.equals(modeloCnpj)) {
            throw new IllegalArgumentException("A evolução não pertence à entidade autenticada.");
        }
    }
    
}
