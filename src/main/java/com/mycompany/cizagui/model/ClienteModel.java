/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;

import com.mycompany.cizagui.contracts.model.IClienteModel;
import com.mycompany.cizagui.exeptions.ClienteException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author ary
 */
@Entity
@Table(name = "cliente")
public class ClienteModel implements IClienteModel{
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "entidade_cnpj", updatable = false)
    private String entidadeCnpj;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false)
    private String email;

    protected ClienteModel() {
    }

    public ClienteModel(String entidadeCnpj, String nome, String cpf, LocalDate dataNascimento, String telefone, String email) throws ClienteException {
        this.id = UUID.randomUUID();
        this.entidadeCnpj = entidadeCnpj;
        this.nome = nome; 
        this.cpf = cpf; 
        this.dataNascimento = dataNascimento; 
        this.telefone = telefone; 
        this.email = email;
        validar(this.id, this.entidadeCnpj, this.nome, this.cpf, this.dataNascimento, this.telefone, this.email);
    }
    
    public ClienteModel(UUID id, String entidadeCnpj, String nome, String cpf, LocalDate dataNascimento, String telefone, String email) throws ClienteException {
        this.id = id;
        this.entidadeCnpj = entidadeCnpj;
        this.nome = nome; 
        this.cpf = cpf; 
        this.dataNascimento = dataNascimento; 
        this.telefone = telefone; 
        this.email = email;
        validar(this.id, this.entidadeCnpj, this.nome, this.cpf, this.dataNascimento, this.telefone, this.email);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getEntidadeCnpj() {
        return entidadeCnpj;
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
    public LocalDate getDataNascimento() {
        return dataNascimento;
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
    public void validaCliente() throws ClienteException {
        validar(id, entidadeCnpj, nome, cpf, dataNascimento, telefone, email);
    }
    
    private static void validar(UUID id,
                            String entidadeCnpj,
                            String nome,
                            String cpf,
                            LocalDate dataNascimento,
                            String telefone,
                            String email) throws ClienteException {

        if (id == null)               throw new ClienteException("ID é obrigatório");
        if (entidadeCnpj == null || entidadeCnpj.isBlank()) throw new ClienteException("Entidade do cliente é obrigatória");
        if (nome == null)             throw new ClienteException("Nome é obrigatório");
        if (cpf == null)              throw new ClienteException("CPF é obrigatório");
        if (dataNascimento == null)   throw new ClienteException("Data de nascimento é obrigatória");
        if (telefone == null)         throw new ClienteException("Telefone é obrigatório");
        if (email == null)            throw new ClienteException("E-mail é obrigatório");
    }
}
