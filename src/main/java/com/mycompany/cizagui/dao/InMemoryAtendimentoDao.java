/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import com.mycompany.cizagui.model.AtendimentoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;

/**
 *
 * @author ary
 */
public class InMemoryAtendimentoDao implements IAtendimentoDao{
    private final List<IAtendimentoModel> atendimentos = new ArrayList<>();
    
    @Override
    public void salvar(IAtendimentoModel atendimento) throws AtendimentoException {
        if (atendimento.ocupaHorario()) {
            boolean conflito = atendimentos.stream()
                    .filter(existente -> !existente.getId().equals(atendimento.getId()))
                    .filter(existente -> existente.getClienteId().equals(atendimento.getClienteId()))
                    .filter(IAtendimentoModel::ocupaHorario)
                    .anyMatch(existente -> AtendimentoModel.horariosConflitam(existente, atendimento));
            if (conflito) {
                throw new AtendimentoException(
                        "Já existe um atendimento para este cliente em horário conflitante");
            }
        }
        atendimentos.add(atendimento);
    }

    @Override
    public Optional<IAtendimentoModel> buscarPorId(UUID id) {
        return atendimentos.stream().filter(item -> item.getId().equals(id)).findFirst();
    }

    @Override
    public void atualizar(IAtendimentoModel atendimento) throws AtendimentoException {
        for (int indice = 0; indice < atendimentos.size(); indice++) {
            if (atendimentos.get(indice).getId().equals(atendimento.getId())) {
                atendimentos.set(indice, atendimento);
                return;
            }
        }
        throw new AtendimentoException("Atendimento não encontrado: " + atendimento.getId());
    }

    @Override
    public List<IAtendimentoModel> listarTodos() {
        return List.copyOf(atendimentos);
    }
    
}
