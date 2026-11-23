/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.contracts.controller.IMenuPrincipalController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.view.MenuPrincipalView;

/**
 * Controlador do menu principal DESCARTÁVEL, apenas para testes e
 * desenvolvimento.
 *
 * @author ary
 */
public class MenuPrincipalController implements IMenuPrincipalController {
    private MenuPrincipalView view;
    private INavegacao navegacao;

    public MenuPrincipalController(INavegacao navegacao) {
        this.view = new MenuPrincipalView();
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnClientes(e -> acaoClientes());
        view.adicionarAcaoBtnAtendimentos(e -> acaoAtendimentos());
        view.adicionarAcaoBtnSair(e -> acaoSair());
    }

    @Override
    public void acaoClientes() {
        this.navegacao.exibirConsultaClientes();
    }

    @Override
    public void acaoAtendimentos() {
        this.navegacao.exibirConsultaAtendimentos();
    }

    @Override
    public void acaoSair() {
        this.navegacao.exibirLogin();
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }
}
