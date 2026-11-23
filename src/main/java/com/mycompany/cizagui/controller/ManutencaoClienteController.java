/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.contracts.controller.IManutencaoClienteController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.ClienteException;
import com.mycompany.cizagui.model.ClienteModel;
import com.mycompany.cizagui.view.ManutencaoClienteView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author ary
 */
public class ManutencaoClienteController implements IManutencaoClienteController {
    private ManutencaoClienteView view;
    private IClienteModel clienteSelecionado;
    private IClienteDao clienteDao;
    private INavegacao navegacao;

    public ManutencaoClienteController(IClienteDao clienteDao, INavegacao navegacao) {
        this.view = new ManutencaoClienteView();
        this.clienteDao = clienteDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnCadastrar(e -> acaoCadastrar());
        view.adicionarAcaoBtnExcluir(e -> acaoExcluir());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
    }

    @Override
    public void prepararTela(IClienteModel cliente) {
        this.clienteSelecionado = cliente;
        if (cliente == null) {
            this.view.limparCampos();
        } else {
            this.view.preencherCampos(
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getDataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    cliente.getTelefone(),
                    cliente.getEmail());
        }
    }

    @Override
    public void acaoCadastrar() {
        try {
            IClienteModel cliente = this.getDadosClienteFromView();
            if (this.clienteSelecionado == null) {
                this.clienteDao.salvar(cliente);
                this.view.exibirMensagem("Cliente cadastrado com sucesso!");
            } else {
                this.clienteDao.excluir(this.clienteSelecionado.getId());
                this.clienteDao.salvar(cliente);
                this.view.exibirMensagem("Cliente atualizado com sucesso!");
            }
            this.clienteSelecionado = null;
            this.view.limparCampos();
            this.navegacao.exibirConsultaClientes();
        } catch (ClienteException | IllegalArgumentException e) {
            view.exibirMensagem(e.getMessage());
        }
    }

    @Override
    public void acaoExcluir() {
        if (this.clienteSelecionado == null) {
            this.view.exibirMensagem("Nenhum cliente selecionado para exclusão.");
            return;
        }
        try {
            this.clienteDao.excluir(this.clienteSelecionado.getId());
        } catch (ClienteException e) {
            this.view.exibirMensagem(e.getMessage());
            return;
        }
        this.clienteSelecionado = null;
        this.view.exibirMensagem("Cliente excluído com sucesso!");
        this.view.limparCampos();
        this.navegacao.exibirConsultaClientes();
    }

    @Override
    public void acaoVoltar() {
        this.clienteSelecionado = null;
        this.view.limparCampos();
        this.navegacao.exibirConsultaClientes();
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }

    private IClienteModel getDadosClienteFromView() throws ClienteException {
        String nome = view.getNome();
        String cpf = view.getCpf();
        String dataNasc = view.getDataNasc();
        String tel = view.getTel();
        String email = view.getEmail();

        if (nome.isBlank() || cpf.isBlank() || dataNasc.isBlank() || tel.isBlank() || email.isBlank()) {
            throw new ClienteException("Todos os campos do cliente devem ser preenchidos.");
        }
        LocalDate dataNascimento = parseDataNascimento(dataNasc);
        if (this.clienteSelecionado != null) {
            return new ClienteModel(this.clienteSelecionado.getId(), nome, cpf, dataNascimento, tel, email);
        }
        return new ClienteModel(nome, cpf, dataNascimento, tel, email);
    }

    private LocalDate parseDataNascimento(String data) throws ClienteException {
        try {
            return LocalDate.parse(data.trim());
        } catch (DateTimeParseException e) {
            throw new ClienteException("A data de nascimento deve estar no formato AAAA-MM-DD.");
        }
    }
}
