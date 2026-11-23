/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.controller;

import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.model.SessaoUsuario;

/**
 *
 * @author ary
 */
public interface INavegacao {
    void exibirLogin();
    void exibirCadastroEntidade();
    void exibirTelaPrincipal(SessaoUsuario sessao);
    void exibirMenuPrincipal();
    void exibirConsultaClientes();
    void exibirManutencaoCliente(IClienteModel cliente);
    void exibirConsultaAtendimentos();
    void exibirCadastroAtendimento();
    void exibirManutencaoAtendimento(IAtendimentoModel atendimento);
    SessaoUsuario getSessaoAtual();
}
