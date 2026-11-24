/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.model;
import com.mycompany.cizagui.exeptions.ClienteException;
import java.util.UUID;
import java.time.LocalDate;
/**
 *
 * @author ary
 */
public interface IClienteModel {
    UUID getId();
    String getEntidadeCnpj();
    String getNome();
    String getCpf();
    LocalDate getDataNascimento();
    String getTelefone();
    String getEmail();
    
    void validaCliente() throws ClienteException;
}
