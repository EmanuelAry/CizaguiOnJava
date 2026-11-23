/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.model;

import com.mycompany.cizagui.exeptions.EntidadeException;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IEntidadeModel {
    UUID getId();
    String getNome();
    String getCnpj();
    
    void validaAtendimento() throws EntidadeException;
}
