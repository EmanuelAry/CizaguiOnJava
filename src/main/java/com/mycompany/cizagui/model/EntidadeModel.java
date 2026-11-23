/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;

import com.mycompany.cizagui.contracts.model.IEntidadeModel;
import com.mycompany.cizagui.exeptions.EntidadeException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author ary
 */
@Entity
@Table(name = "entidade")
public class EntidadeModel implements IEntidadeModel{
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cnpj", nullable = false, length = 14, unique = true)
    private String cnpj;

    /**
     * Construtor exigido pelo JPA. Não valida, pois os dados vêm do banco.
     */
    protected EntidadeModel() {
    }

    public EntidadeModel(String nome, String cnpj) throws EntidadeException{
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.cnpj = cnpj;
        validar(id, nome, cnpj);
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
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public void validaAtendimento() throws EntidadeException {
        validar(id, nome, cnpj); 
    }
    
    private static void validar(UUID id,
                            String nome,
                            String cnpj) throws EntidadeException {

        if (id == null)               throw new EntidadeException("Identificador é obrigatório");
        if (nome == null)             throw new EntidadeException("Nome da Entidade é obrigatório");
        if (cnpj == null || cnpj.length() != 14 || !cnpj.chars().allMatch(Character::isDigit)) {
            throw new EntidadeException("CNPJ deve conter 14 dígitos");
        }
    }
}
