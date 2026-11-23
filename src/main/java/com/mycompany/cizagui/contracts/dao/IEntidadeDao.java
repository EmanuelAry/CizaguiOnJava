/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.dao;

import com.mycompany.cizagui.contracts.model.IEntidadeModel;
import com.mycompany.cizagui.exeptions.EntidadeException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IEntidadeDao {
    boolean estaVazio();
    Optional<IEntidadeModel> buscarPorCnpj(String cnpj);
    Optional<IEntidadeModel> buscarPorId(UUID id);
    List<IEntidadeModel> listarTodas();
    void salvar(IEntidadeModel entidade) throws EntidadeException;
}
