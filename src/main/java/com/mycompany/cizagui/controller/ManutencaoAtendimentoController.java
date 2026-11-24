/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.Enum.StatusAtendimentoEnum;
import com.mycompany.cizagui.contracts.controller.IManutencaoAtendimentoController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.dao.IProntuarioDao;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import com.mycompany.cizagui.exeptions.EvolucaoException;
import com.mycompany.cizagui.model.EvolucaoModel;
import com.mycompany.cizagui.view.ManutencaoAtendimentoView;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author ary
 */
public class ManutencaoAtendimentoController implements IManutencaoAtendimentoController {
    private ManutencaoAtendimentoView view;
    private IAtendimentoModel atendimentoSelecionado;
    private IAtendimentoDao atendimentoDao;
    private IClienteDao clienteDao;
    private IProntuarioDao prontuarioDao;
    private INavegacao navegacao;

    public ManutencaoAtendimentoController(IAtendimentoDao atendimentoDao, IClienteDao clienteDao,
                                          IProntuarioDao prontuarioDao, INavegacao navegacao) {
        this.view = new ManutencaoAtendimentoView();
        this.clienteDao = clienteDao;
        this.atendimentoDao = atendimentoDao;
        this.prontuarioDao = prontuarioDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnConcluir(e -> acaoConcluir());
        view.adicionarAcaoBtnCancelado(e -> acaoCancelado());
        view.adicionarAcaoBtnNCompareceu(e -> acaoNaoCompareceu());
        view.adicionarAcaoBtnRemarcado(e -> acaoRemarcado());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
    }

    @Override
    public void prepararTela(IAtendimentoModel atendimento) {
        this.atendimentoSelecionado = atendimento;
        this.view.limparCampos();
        this.view.setCliente(this.nomeCliente(atendimento.getClienteId()));
        this.view.setProcedimento(atendimento.getProcedimento());
        this.view.setProfissional(atendimento.getProfissional());
    }

    @Override
    public void acaoConcluir() {
        this.alterarStatus(StatusAtendimentoEnum.CONCLUIDO, true);
    }

    @Override
    public void acaoCancelado() {
        this.alterarStatus(StatusAtendimentoEnum.CANCELADO, false);
    }

    @Override
    public void acaoNaoCompareceu() {
        this.alterarStatus(StatusAtendimentoEnum.NAO_COMPARECEU, false);
    }

    @Override
    public void acaoRemarcado() {
        this.alterarStatus(StatusAtendimentoEnum.REMARCADO, false);
    }

    @Override
    public void acaoVoltar() {
        this.atendimentoSelecionado = null;
        this.view.limparCampos();
        this.navegacao.exibirConsultaAtendimentos();
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.view.setVisible(b);
    }

    private void alterarStatus(StatusAtendimentoEnum novoStatus, boolean evolucaoObrigatoria) {
        if (this.atendimentoSelecionado == null) {
            this.view.exibirMensagem("Nenhum atendimento selecionado.");
            return;
        }
        String textoEvolucao = view.getEvolucao();
        if (evolucaoObrigatoria && textoEvolucao.isBlank()) {
            this.view.exibirMensagem("Para concluir o Atendimento é obrigatório informar a Evolução.");
            return;
        }
        try {
            this.atendimentoSelecionado.atualizaStatus(novoStatus);
            this.atendimentoDao.atualizar(this.navegacao.exigirCnpjEntidadeAtual(), this.atendimentoSelecionado);
            if (!textoEvolucao.isBlank()) {
                this.registrarEvolucao(textoEvolucao);
            }
            this.view.exibirMensagem("Atendimento atualizado para " + novoStatus.name() + " com sucesso!");
            this.atendimentoSelecionado = null;
            this.view.limparCampos();
            this.navegacao.exibirConsultaAtendimentos();
        } catch (AtendimentoException | EvolucaoException e) {
            view.exibirMensagem(e.getMessage());
        }
    }

    private void registrarEvolucao(String textoEvolucao) throws EvolucaoException {
        String autorNome = this.navegacao.getSessaoAtual() != null
                ? this.navegacao.getSessaoAtual().getNome()
                : "Administrador";
        this.prontuarioDao.salvar(this.navegacao.exigirCnpjEntidadeAtual(), new EvolucaoModel(
                this.navegacao.exigirCnpjEntidadeAtual(), this.atendimentoSelecionado.getId(),
                textoEvolucao, autorNome, LocalDateTime.now()));
    }
    
    private String nomeCliente(UUID clienteId) {
        return this.clienteDao.buscarPorId(this.navegacao.exigirCnpjEntidadeAtual(), clienteId)
                .map(IClienteModel::getNome)
                .orElse("(cliente removido)");
    }
}
