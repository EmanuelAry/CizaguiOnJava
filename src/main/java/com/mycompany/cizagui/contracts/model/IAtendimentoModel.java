/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.model;

import java.time.LocalDateTime;
import java.util.UUID;
import com.mycompany.cizagui.Enum.StatusAtendimentoEnum;
import com.mycompany.cizagui.exeptions.AtendimentoException;
/**
 *
 * @author ary
 */
public interface IAtendimentoModel {
    UUID getId();
    UUID getClienteId();
    String getProcedimento();
    String getProfissional();
    LocalDateTime getInicio();
    LocalDateTime getFim();
    StatusAtendimentoEnum getStatus();

    void atualizaStatus(StatusAtendimentoEnum novoStatus);
    void validaAtendimento() throws AtendimentoException;
    boolean ocupaHorario();
}
