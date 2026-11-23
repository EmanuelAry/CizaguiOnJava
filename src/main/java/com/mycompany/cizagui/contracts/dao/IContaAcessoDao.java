/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.dao;

import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import com.mycompany.cizagui.exeptions.ContaAcessoException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ary
 */
public interface IContaAcessoDao {
    boolean estaVazio();
    Optional<IContaAcessoModel> buscarPorCnpjEmail(String cnpj, String email);
    List<IContaAcessoModel> listarPorCnpj(String cnpj);
    void salvar(IContaAcessoModel conta) throws ContaAcessoException;
}
