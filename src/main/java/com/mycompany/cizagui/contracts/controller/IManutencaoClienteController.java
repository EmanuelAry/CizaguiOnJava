/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.controller;

import com.mycompany.cizagui.contracts.model.IClienteModel;

/**
 *
 * @author ary
 */
public interface IManutencaoClienteController {
    void adicionarAcoes();
    void acaoCadastrar();
    void acaoExcluir();
    void acaoVoltar();
    void prepararTela(IClienteModel cliente);
    void mudaVisibilidade(boolean b);
}
