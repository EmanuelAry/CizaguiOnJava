/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.dao;

import com.mycompany.cizagui.contracts.model.IEvolucaoModel;
import com.mycompany.cizagui.exeptions.EvolucaoException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IProntuarioDao {
    void salvar(String entidadeCnpj, IEvolucaoModel evolucao) throws EvolucaoException;
    Optional<IEvolucaoModel> buscarEvolucaoPorId(String entidadeCnpj, UUID evolucaoId);
    Optional<IEvolucaoModel> buscarEvolucaoPorAtendimento(String entidadeCnpj, UUID atendimentoId);
    List<IEvolucaoModel> listarEvolucoes(String entidadeCnpj);
    /* capei fora para diminuir o escopo */
    //void salvar(AnotacaoComplementar anotacao);
    //List<AnotacaoComplementar> listarAnotacoes(UUID evolucaoId);
}
