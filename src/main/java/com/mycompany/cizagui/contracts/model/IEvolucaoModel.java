/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.model;

import com.mycompany.cizagui.exeptions.EvolucaoException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IEvolucaoModel {
    UUID getId();
    String getEntidadeCnpj();
    UUID getAtendimentoId();
    String getTexto();
    String getAutorNome();
    LocalDateTime getRegistradaEm();
    
    
    void validaEvolucao() throws EvolucaoException;
}
