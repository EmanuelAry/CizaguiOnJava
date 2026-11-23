/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.controller;

import com.mycompany.cizagui.contracts.model.IAtendimentoModel;

/**
 *
 * @author ary
 */
public interface IManutencaoAtendimentoController {
    void adicionarAcoes();
    void acaoConcluir();
    void acaoCancelado();
    void acaoNaoCompareceu();
    void acaoRemarcado();
    void acaoVoltar();
    void prepararTela(IAtendimentoModel atendimento);
    void mudaVisibilidade(boolean b);
}
