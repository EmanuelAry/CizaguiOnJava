/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.cizagui.contracts.model;

import com.mycompany.cizagui.Enum.PerfilEnum;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import com.mycompany.cizagui.model.SessaoUsuario;
import java.util.UUID;

/**
 *
 * @author ary
 */
public interface IContaAcessoModel {
    UUID getId();
    String getNome();
    String getCpf();
    String getCnpj();
    String getTelefone();
    String getEmail();
    PerfilEnum getPerfil();
    byte[] getSalt();
    byte[] getSenhaHash();
    byte[] salt();
    byte[] senhaHash();
    
    SessaoUsuario abrirSessao();    
}
