/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;

import com.mycompany.cizagui.Enum.PerfilEnum;
import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author ary
 */
@Entity
@Table(name = "conta_acesso")
public class ContaAcessoModel implements IContaAcessoModel {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private PerfilEnum perfil;

    @Column(name = "salt", nullable = false)
    private byte[] salt; //emanuel validar funcionamento verificar alternativa mais simples

    @Column(name = "senha_hash", nullable = false)
    private byte[] senhaHash;

    protected ContaAcessoModel() {
    }

    public ContaAcessoModel(String nome, String cpf, String cnpj, String telefone, String email, PerfilEnum perfil, byte[] salt, byte[] senhaHash) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
        this.perfil = perfil;
        this.salt = salt;
        this.senhaHash = senhaHash;
    }

    
    
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getCpf() {
        return cpf;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getTelefone() {
        return telefone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public PerfilEnum getPerfil() {
        return perfil;
    }

    @Override
    public byte[] getSalt() {
        return salt;
    }

    @Override
    public byte[] getSenhaHash() {
        return senhaHash;
    }
    
    @Override
    public byte[] salt() {
        return salt.clone();
    }

    @Override
    public byte[] senhaHash() {
        return senhaHash.clone();
    }
    
    @Override
    public SessaoUsuario abrirSessao() {
        return new SessaoUsuario(id, nome, cnpj, email, perfil);
    }
}
