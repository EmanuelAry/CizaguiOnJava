/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;

import com.mycompany.cizagui.contracts.model.IEvolucaoModel;
import com.mycompany.cizagui.exeptions.EvolucaoException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author ary
 */
@Entity
@Table(name = "evolucao")
public class EvolucaoModel implements IEvolucaoModel{
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "entidade_cnpj", updatable = false)
    private String entidadeCnpj;

    @Column(name = "atendimento_id", nullable = false)
    private UUID atendimentoId;

    @Column(name = "texto", nullable = false, length = 4000)
    private String texto;

    @Column(name = "autor_nome", nullable = false)
    private String autorNome;

    @Column(name = "registrada_em", nullable = false)
    private LocalDateTime registradaEm;

    protected EvolucaoModel() {
    }

    public EvolucaoModel(String entidadeCnpj, UUID atendimentoId, String texto, String autorNome, LocalDateTime registradaEm) throws EvolucaoException {
        this.id = UUID.randomUUID();
        this.entidadeCnpj = entidadeCnpj;
        this.atendimentoId = atendimentoId;
        this.texto = texto;
        this.autorNome = autorNome;
        this.registradaEm = registradaEm;
        validar(id, entidadeCnpj, atendimentoId, texto, autorNome, registradaEm);
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
    public UUID getAtendimentoId() {
        return atendimentoId;
    }

    @Override
    public String getTexto() {
        return texto;
    }

    @Override
    public String getAutorNome() {
        return autorNome;
    }

    @Override
    public LocalDateTime getRegistradaEm() {
        return registradaEm;
    }

    @Override
    public void validaEvolucao() throws EvolucaoException {
        validar(id, entidadeCnpj, atendimentoId, texto, autorNome, registradaEm);
    }
    
    private static void validar(UUID id,
                                String entidadeCnpj,
                                UUID atendimentoId, 
                                String texto, 
                                String autorNome, 
                                LocalDateTime registradaEm) throws EvolucaoException {

        if (id == null)               throw new EvolucaoException("Identificador é obrigatório");
        if (entidadeCnpj == null || entidadeCnpj.isBlank()) throw new EvolucaoException("Entidade da evolução é obrigatória");
        if (atendimentoId == null)    throw new EvolucaoException("ID do Atendimento é obrigatório");
        if (texto == null)            throw new EvolucaoException("Texto é obrigatório");
        if (autorNome == null)        throw new EvolucaoException("Nome do autor é obrigatório");
        if (registradaEm == null)     throw new EvolucaoException("Data e hora do registro são obrigatórias");
    }
}
