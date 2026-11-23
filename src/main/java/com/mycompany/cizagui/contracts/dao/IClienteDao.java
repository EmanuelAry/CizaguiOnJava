/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.dao;

import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.ClienteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IClienteDao {
    void salvar(IClienteModel cliente) throws ClienteException;
    Optional<IClienteModel> buscarPorCpf(String cpf);
    Optional<IClienteModel> buscarPorId(UUID id);
    void excluir(UUID id) throws ClienteException;
    List<IClienteModel> listarTodos();
}
