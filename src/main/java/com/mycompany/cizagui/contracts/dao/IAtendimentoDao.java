/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.dao;

import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IAtendimentoDao {
    void salvar(String entidadeCnpj, IAtendimentoModel atendimento) throws AtendimentoException;
    Optional<IAtendimentoModel> buscarPorId(String entidadeCnpj, UUID id);
    void atualizar(String entidadeCnpj, IAtendimentoModel atendimento) throws AtendimentoException;
    List<IAtendimentoModel> listarTodos(String entidadeCnpj);
}
