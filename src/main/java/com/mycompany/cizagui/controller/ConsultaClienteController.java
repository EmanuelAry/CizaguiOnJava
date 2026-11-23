/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.contracts.controller.IConsultaClienteController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.view.ConsultaClienteView;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author ary
 */
public class ConsultaClienteController implements IConsultaClienteController {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ConsultaClienteView view;
    private IClienteDao clienteDao;
    private INavegacao navegacao;
    private List<IClienteModel> clientes;

    public ConsultaClienteController(IClienteDao clienteDao, INavegacao navegacao) {
        this.view = new ConsultaClienteView();
        this.clienteDao = clienteDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnNovoCliente(e -> acaoNovoCliente());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
        view.adicionarAcaoSelecionarCliente(e -> {
            if (!e.getValueIsAdjusting()) {
                acaoSelecionarCliente();
            }
        });
    }

    @Override
    public void acaoNovoCliente() {
        this.navegacao.exibirManutencaoCliente(null);
    }

    @Override
    public void acaoSelecionarCliente() {
        int linha = view.getLinhaClienteSelecionada();
        if (linha < 0 || this.clientes == null || linha >= this.clientes.size()) {
            return;
        }
        this.navegacao.exibirManutencaoCliente(this.clientes.get(linha));
    }

    @Override
    public void acaoVoltar() {
        this.navegacao.exibirMenuPrincipal();
    }

    @Override
    public void atualizarListagem() {
        this.clientes = this.clienteDao.listarTodos();
        String[][] linhas = this.clientes.stream()
                .map(cliente -> new String[]{
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getDataNascimento().format(FORMATO_DATA),
                    cliente.getTelefone(),
                    cliente.getEmail()
                })
                .toArray(String[][]::new);
        this.view.atualizarListagem(linhas);
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }
}
