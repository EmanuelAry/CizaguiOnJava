/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.Enum.StatusAtendimentoEnum;
import com.mycompany.cizagui.contracts.controller.IConsultaAtendimentoController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.dao.IProntuarioDao;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.contracts.model.IEvolucaoModel;
import com.mycompany.cizagui.view.ConsultaAtendimentoView;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author ary
 */
public class ConsultaAtendimentoController implements IConsultaAtendimentoController {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ConsultaAtendimentoView view;
    private IAtendimentoDao atendimentoDao;
    private IClienteDao clienteDao;
    private IProntuarioDao prontuarioDao;
    private INavegacao navegacao;
    private List<IClienteModel> clientes;
    private List<IAtendimentoModel> atendimentos;

    public ConsultaAtendimentoController(IAtendimentoDao atendimentoDao, IClienteDao clienteDao, IProntuarioDao prontuarioDao, INavegacao navegacao) {
        this.view = new ConsultaAtendimentoView();
        this.atendimentoDao = atendimentoDao;
        this.clienteDao = clienteDao;
        this.prontuarioDao = prontuarioDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnNovoAtendimento(e -> acaoNovoAtendimento());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
        view.adicionarAcaoFiltroProfissional(e -> atualizarListagem());
        view.adicionarAcaoFiltroCliente(e -> atualizarListagem());
        view.adicionarAcaoFiltroStatus(e -> atualizarListagem());
        view.adicionarAcaoSelecionarAtendimento(e -> {
            if (!e.getValueIsAdjusting()) {
                acaoSelecionarAtendimento();
            }
        });
    }

    @Override
    public void acaoNovoAtendimento() {
        this.navegacao.exibirCadastroAtendimento();
    }

    @Override
    public void acaoVoltar() {
        this.navegacao.exibirMenuPrincipal();
    }

    @Override
    public void acaoSelecionarAtendimento() {
        int linha = view.getLinhaAtendimentoSelecionada();
        if (linha < 0 || this.atendimentos == null || linha >= this.atendimentos.size()) {
            return;
        }
        IAtendimentoModel atendimento = this.atendimentos.get(linha);
        if (atendimento.getStatus() != StatusAtendimentoEnum.AGENDADO) {
            this.view.exibirMensagem("Só é possível manutenção de atendimentos com status Agendado.");
            this.carregarEvolucao(atendimento);
            return;
        }
        this.view.setEvolucao("");
        this.navegacao.exibirManutencaoAtendimento(atendimento);
    }

    private void carregarEvolucao(IAtendimentoModel atendimento) {
        String texto = this.prontuarioDao.buscarEvolucaoPorAtendimento(atendimento.getId())
                .map(IEvolucaoModel::getTexto)
                .orElse("");
        this.view.setEvolucao(texto);
    }

    @Override
    public void prepararTela() {
        this.clientes = this.clienteDao.listarTodos();
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Todos");
        opcoes.addAll(this.clientes.stream().map(IClienteModel::getNome).toList());
        this.view.carregarClientes(opcoes);
        this.view.selecionarTodosStatus();
        this.atualizarListagem();
    }

    @Override
    public void atualizarListagem() {
        String filtroProfissional = view.getFiltroProfissional().trim().toLowerCase();
        Set<StatusAtendimentoEnum> statusSelecionados = this.getStatusSelecionadosFromView();
        UUID clienteFiltroId = this.getClienteFiltroIdFromView();

        this.atendimentos = this.atendimentoDao.listarTodos().stream()
                .filter(atendimento -> filtroProfissional.isEmpty()
                        || atendimento.getProfissional().toLowerCase().contains(filtroProfissional))
                .filter(atendimento -> statusSelecionados.contains(atendimento.getStatus()))
                .filter(atendimento -> clienteFiltroId == null
                        || atendimento.getClienteId().equals(clienteFiltroId))
                .sorted(java.util.Comparator.comparing(IAtendimentoModel::getInicio).reversed())
                .toList();
        String[][] linhas = this.atendimentos.stream()
                .map(atendimento -> new String[]{
                    this.nomeCliente(atendimento.getClienteId()),
                    atendimento.getProfissional(),
                    atendimento.getProcedimento(),
                    atendimento.getInicio().format(FORMATO_DATA_HORA),
                    atendimento.getFim().format(FORMATO_DATA_HORA),
                    atendimento.getStatus().name()
                })
                .toArray(String[][]::new);
        this.view.atualizarListagem(linhas);
        this.view.setEvolucao("");
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }

    private Set<StatusAtendimentoEnum> getStatusSelecionadosFromView() {
        Set<StatusAtendimentoEnum> status = EnumSet.noneOf(StatusAtendimentoEnum.class);
        if (view.isCheckAgendado())      status.add(StatusAtendimentoEnum.AGENDADO);
        if (view.isCheckCancelado())     status.add(StatusAtendimentoEnum.CANCELADO);
        if (view.isCheckConcluido())     status.add(StatusAtendimentoEnum.CONCLUIDO);
        if (view.isCheckNaoCompareceu()) status.add(StatusAtendimentoEnum.NAO_COMPARECEU);
        if (view.isCheckRemarcado())     status.add(StatusAtendimentoEnum.REMARCADO);
        return status;
    }

    private UUID getClienteFiltroIdFromView() {
        int indice = view.getClienteSelecionadoIndex();
        if (this.clientes == null || indice <= 0 || indice > this.clientes.size()) {
            return null;
        }
        return this.clientes.get(indice - 1).getId();
    }

    private String nomeCliente(UUID clienteId) {
        return this.clienteDao.buscarPorId(clienteId)
                .map(IClienteModel::getNome)
                .orElse("(cliente removido)");
    }
}
