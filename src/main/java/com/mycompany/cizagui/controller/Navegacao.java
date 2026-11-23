/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.contracts.controller.ICadastrarEntidadeController;
import com.mycompany.cizagui.contracts.controller.ICadastroAtendimentoController;
import com.mycompany.cizagui.contracts.controller.IConsultaAtendimentoController;
import com.mycompany.cizagui.contracts.controller.IConsultaClienteController;
import com.mycompany.cizagui.contracts.controller.ILoginController;
import com.mycompany.cizagui.contracts.controller.IManutencaoAtendimentoController;
import com.mycompany.cizagui.contracts.controller.IManutencaoClienteController;
import com.mycompany.cizagui.contracts.controller.IMenuPrincipalController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;
import com.mycompany.cizagui.contracts.dao.IEntidadeDao;
import com.mycompany.cizagui.contracts.dao.IProntuarioDao;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.model.SessaoUsuario;

/**
 * Centraliza as transições de tela da aplicação. Os controladores conhecem
 * apenas este contrato de navegação, nunca uns aos outros.
 *
 * @author ary
 */
public class Navegacao implements INavegacao {
    private final ILoginController loginController;
    private final ICadastrarEntidadeController cadastrarEntidadeController;
    private final IMenuPrincipalController menuPrincipalController;
    private final IConsultaAtendimentoController consultaAtendimentoController;
    private final ICadastroAtendimentoController cadastroAtendimentoController;
    private final IConsultaClienteController consultaClienteController;
    private final IManutencaoClienteController manutencaoClienteController;
    private final IManutencaoAtendimentoController manutencaoAtendimentoController;
    private SessaoUsuario sessaoAtual;

    public Navegacao(IEntidadeDao entidadeDao, IContaAcessoDao contaDao,
                     IAtendimentoDao atendimentoDao, IClienteDao clienteDao,
                     IProntuarioDao prontuarioDao) {
        this.loginController = new LoginController(contaDao, entidadeDao, this);
        this.cadastrarEntidadeController = new CadastrarEntidadeController(entidadeDao, contaDao, this);
        this.menuPrincipalController = new MenuPrincipalController(this);
        this.consultaAtendimentoController = new ConsultaAtendimentoController(atendimentoDao, clienteDao, prontuarioDao, this);
        this.cadastroAtendimentoController = new CadastroAtendimentoController(atendimentoDao, clienteDao, this);
        this.consultaClienteController = new ConsultaClienteController(clienteDao, this);
        this.manutencaoClienteController = new ManutencaoClienteController(clienteDao, this);
        this.manutencaoAtendimentoController = new ManutencaoAtendimentoController(atendimentoDao, prontuarioDao, this);
    }

    @Override
    public SessaoUsuario getSessaoAtual() {
        return sessaoAtual;
    }

    @Override
    public void exibirLogin() {
        this.sessaoAtual = null;
        this.cadastrarEntidadeController.mudaVisibilidade(false);
        this.menuPrincipalController.mudaVisibilidade(false);
        this.consultaAtendimentoController.mudaVisibilidade(false);
        this.cadastroAtendimentoController.mudaVisibilidade(false);
        this.consultaClienteController.mudaVisibilidade(false);
        this.manutencaoClienteController.mudaVisibilidade(false);
        this.manutencaoAtendimentoController.mudaVisibilidade(false);
        this.loginController.mudaVisibilidade(true);
    }

    @Override
    public void exibirCadastroEntidade() {
        this.cadastrarEntidadeController.mudaVisibilidade(true);
        this.loginController.mudaVisibilidade(false);
    }

    @Override
    public void exibirTelaPrincipal(SessaoUsuario sessao) {
        this.sessaoAtual = sessao;
        this.loginController.mudaVisibilidade(false);
        this.exibirMenuPrincipal();
    }

    @Override
    public void exibirMenuPrincipal() {
        this.consultaClienteController.mudaVisibilidade(false);
        this.manutencaoClienteController.mudaVisibilidade(false);
        this.consultaAtendimentoController.mudaVisibilidade(false);
        this.cadastroAtendimentoController.mudaVisibilidade(false);
        this.manutencaoAtendimentoController.mudaVisibilidade(false);
        this.menuPrincipalController.mudaVisibilidade(true);
    }

    @Override
    public void exibirConsultaClientes() {
        this.menuPrincipalController.mudaVisibilidade(false);
        this.manutencaoClienteController.mudaVisibilidade(false);
        this.consultaAtendimentoController.mudaVisibilidade(false);
        this.consultaClienteController.atualizarListagem();
        this.consultaClienteController.mudaVisibilidade(true);
    }

    @Override
    public void exibirManutencaoCliente(IClienteModel cliente) {
        this.consultaClienteController.mudaVisibilidade(false);
        this.manutencaoClienteController.prepararTela(cliente);
        this.manutencaoClienteController.mudaVisibilidade(true);
    }

    @Override
    public void exibirConsultaAtendimentos() {
        this.menuPrincipalController.mudaVisibilidade(false);
        this.consultaClienteController.mudaVisibilidade(false);
        this.cadastroAtendimentoController.mudaVisibilidade(false);
        this.manutencaoAtendimentoController.mudaVisibilidade(false);
        this.consultaAtendimentoController.prepararTela();
        this.consultaAtendimentoController.mudaVisibilidade(true);
    }

    @Override
    public void exibirCadastroAtendimento() {
        this.consultaAtendimentoController.mudaVisibilidade(false);
        this.cadastroAtendimentoController.prepararTela();
        this.cadastroAtendimentoController.mudaVisibilidade(true);
    }

    @Override
    public void exibirManutencaoAtendimento(IAtendimentoModel atendimento) {
        this.consultaAtendimentoController.mudaVisibilidade(false);
        this.manutencaoAtendimentoController.prepararTela(atendimento);
        this.manutencaoAtendimentoController.mudaVisibilidade(true);
    }
}
