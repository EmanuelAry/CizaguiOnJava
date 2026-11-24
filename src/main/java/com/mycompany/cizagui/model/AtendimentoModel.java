/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.model;
import java.time.LocalDateTime;
import java.util.UUID;
import com.mycompany.cizagui.Enum.StatusAtendimentoEnum;
import com.mycompany.cizagui.contracts.model.IAtendimentoModel;
import com.mycompany.cizagui.exeptions.AtendimentoException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 *
 * @author ary
 */
@Entity
@Table(name = "atendimento")
public class AtendimentoModel implements IAtendimentoModel{
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "entidade_cnpj", updatable = false)
    private String entidadeCnpj;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "procedimento", nullable = false)
    private String procedimento;

    @Column(name = "profissional", nullable = false)
    private String profissional;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAtendimentoEnum status;


    protected AtendimentoModel() {
    }

    public AtendimentoModel(String entidadeCnpj, UUID clienteId, String procedimento, String profissional, LocalDateTime inicio, LocalDateTime fim) throws AtendimentoException {
        this.id = UUID.randomUUID();
        this.entidadeCnpj = entidadeCnpj;
        this.clienteId = clienteId; 
        this.procedimento = procedimento;
        this.profissional = profissional;
        this.inicio = inicio;
        this.fim = fim;
        this.status = StatusAtendimentoEnum.AGENDADO;
        validar(this.id, this.entidadeCnpj, this.clienteId, this.procedimento,
            this.profissional, this.inicio, this.fim, this.status);
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
    public UUID getClienteId() {
        return clienteId;
    }

       @Override
    public String getProcedimento() {
        return procedimento;
    }

       @Override
    public String getProfissional() {
        return profissional;
    }

       @Override
    public LocalDateTime getInicio() {
        return inicio;
    }

       @Override
    public LocalDateTime getFim() {
        return fim;
    }

       @Override
    public StatusAtendimentoEnum getStatus() {
        return status;
    }

       @Override
    public void atualizaStatus(StatusAtendimentoEnum novoStatus) {
        this.status = novoStatus;
    }

       @Override
    public void validaAtendimento() throws AtendimentoException {
        validar(id, entidadeCnpj, clienteId, procedimento, profissional, inicio, fim, status);
    }
    
    private static void validar(UUID id,
                            String entidadeCnpj,
                            UUID clienteId,
                            String procedimento,
                            String profissional,
                            LocalDateTime inicio,
                            LocalDateTime fim,
                            StatusAtendimentoEnum status)
        throws AtendimentoException {

    if (id == null)             throw new AtendimentoException("Identificador é obrigatório");
    if (entidadeCnpj == null || entidadeCnpj.isBlank()) throw new AtendimentoException("Entidade do atendimento é obrigatória");
    if (clienteId == null)      throw new AtendimentoException("ID do Cliente é obrigatório");
    if (procedimento == null || procedimento.isBlank()) throw new AtendimentoException("Procedimento é obrigatório");
    if (profissional == null || profissional.isBlank()) throw new AtendimentoException("Profissional é obrigatório");
    if (inicio == null)         throw new AtendimentoException("Horário de início é obrigatório");
    if (fim == null)            throw new AtendimentoException("Horário de término é obrigatório");
    if (status == null)         throw new AtendimentoException("Status é obrigatório");
    if (!fim.isAfter(inicio))   throw new AtendimentoException("O término deve ser posterior ao início");
}

    /**
     * Dois atendimentos conflitam quando seus intervalos [inicio, fim) se
     * intersectam. Intervalos apenas encostados (fim de um == início do
     * outro) não conflitam.
     */
    public static boolean horariosConflitam(IAtendimentoModel a, IAtendimentoModel b) {
        return a.getInicio().isBefore(b.getFim()) && b.getInicio().isBefore(a.getFim());
    }

       @Override
    public boolean ocupaHorario() {
        return status != StatusAtendimentoEnum.CANCELADO && status != StatusAtendimentoEnum.NAO_COMPARECEU;
    }
}
