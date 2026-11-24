/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;

import com.mycompany.cizagui.Enum.PerfilEnum;
import java.util.UUID;

/**
 *
 * @author ary
 */
public class SessaoUsuario {
    UUID id; 
    String nome; 
    String cnpj; 
    String email; 
    PerfilEnum perfil;
    
    public SessaoUsuario(UUID id, String nome, String cnpj, String email, PerfilEnum perfil) {
        this.id = id; 
        this.nome = nome; 
        this.cnpj = cnpj; 
        this.email = email; 
        this.perfil = perfil;
    }
    
    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }
    
    public PerfilEnum getPerfil() {
        return perfil;
    }
}
/**
 * emanuel validar utilizar record
 public record SessaoUsuario(UUID id, String nome, String cnpj, String email, PerfilEnum perfil) {
}

 */
