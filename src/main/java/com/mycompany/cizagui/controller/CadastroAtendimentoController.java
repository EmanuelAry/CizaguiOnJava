/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.contracts.controller.ICadastroAtendimentoController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import com.mycompany.cizagui.model.AtendimentoModel;
import com.mycompany.cizagui.view.CadastroAtendimentoView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author ary
 */
public class CadastroAtendimentoController implements ICadastroAtendimentoController {
    private static final int DURACAO_PADRAO_MINUTOS = 60;

    private CadastroAtendimentoView view;
    private IAtendimentoModel model;
    private IAtendimentoDao atendimentoDao;
    private IClienteDao clienteDao;
    private INavegacao navegacao;
    private List<IClienteModel> clientes;

    public CadastroAtendimentoController(IAtendimentoDao atendimentoDao, IClienteDao clienteDao, INavegacao navegacao) {
        this.view = new CadastroAtendimentoView();
        this.atendimentoDao = atendimentoDao;
        this.clienteDao = clienteDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnSalvar(e -> acaoSalvar());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
    }

    @Override
    public void acaoSalvar() {
        try {
            this.model = this.getDadosAtendimentoFromView();
            this.atendimentoDao.salvar(this.navegacao.exigirCnpjEntidadeAtual(), model);
            this.view.exibirMensagem("Atendimento cadastrado com sucesso!");
            this.view.limparCampos();
            this.navegacao.exibirConsultaAtendimentos();
        } catch (AtendimentoException | IllegalArgumentException e) {
            view.exibirMensagem(e.getMessage());
        }
    }

    @Override
    public void acaoVoltar() {
        this.view.limparCampos();
        this.navegacao.exibirConsultaAtendimentos();
    }

    @Override
    public void prepararTela() {
        this.clientes = this.clienteDao.listarTodos(this.navegacao.exigirCnpjEntidadeAtual());
        this.view.carregarClientes(this.clientes.stream().map(IClienteModel::getNome).toList());
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }

    private IAtendimentoModel getDadosAtendimentoFromView() throws AtendimentoException {
        int indiceCliente = view.getClienteSelecionadoIndex();
        if (this.clientes == null || indiceCliente < 0 || indiceCliente >= this.clientes.size()) {
            throw new AtendimentoException("Selecione um cliente.");
        }
        String procedimento = view.getProcedimento();
        String profissional = view.getProfissional();

        LocalDate data = parseData(view.getData());
        LocalTime hora = parseHora(view.getHora());
        LocalDateTime inicio = LocalDateTime.of(data, hora);
        LocalDateTime fim = inicio.plusMinutes(DURACAO_PADRAO_MINUTOS);  
        if(view.getDuracao() > 0){
            fim = inicio.plusMinutes(view.getDuracao());
        }
        return new AtendimentoModel(this.navegacao.exigirCnpjEntidadeAtual(),
                this.clientes.get(indiceCliente).getId(), procedimento, profissional, inicio, fim);
    }

    private LocalDate parseData(String data) throws AtendimentoException {
        try {
            return LocalDate.parse(data.trim());
        } catch (DateTimeParseException e) {
            throw new AtendimentoException("A data deve estar no formato AAAA-MM-DD.");
        }
    }

    private LocalTime parseHora(String hora) throws AtendimentoException {
        try {
            return LocalTime.parse(hora.trim());
        } catch (DateTimeParseException e) {
            throw new AtendimentoException("A hora deve estar no formato HH:mm.");
        }
    }
}
