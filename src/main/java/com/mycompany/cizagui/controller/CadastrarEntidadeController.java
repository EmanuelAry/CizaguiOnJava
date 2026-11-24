/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.controller;

import com.mycompany.cizagui.Enum.PerfilEnum;
import com.mycompany.cizagui.contracts.controller.ICadastrarEntidadeController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;
import com.mycompany.cizagui.contracts.dao.IEntidadeDao;
import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import com.mycompany.cizagui.contracts.model.IEntidadeModel;
import com.mycompany.cizagui.exeptions.ContaAcessoException;
import com.mycompany.cizagui.exeptions.EntidadeException;
import com.mycompany.cizagui.model.ContaAcessoModel;
import com.mycompany.cizagui.model.EntidadeModel;
import com.mycompany.cizagui.view.CadastrarEntidadeView;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author ary
 */
public class CadastrarEntidadeController implements ICadastrarEntidadeController {
    private CadastrarEntidadeView view;
    private javax.swing.JFrame janela;
    private IEntidadeModel entidadeModel;
    private IContaAcessoModel contaModel;
    private IEntidadeDao entidadeDao;
    private IContaAcessoDao contaDao;
    private INavegacao navegacao;

    public CadastrarEntidadeController(IEntidadeDao entidadeDao, IContaAcessoDao contaDao, INavegacao navegacao) {
        this.view = new CadastrarEntidadeView();
        this.janela = new javax.swing.JFrame("Cadastrar Clínica");
        this.janela.setContentPane(this.view);
        this.janela.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        this.janela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                acaoVoltar();
            }
        });
        this.janela.pack();
        this.janela.setLocationRelativeTo(null);
        this.entidadeDao = entidadeDao;
        this.contaDao = contaDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    @Override
    public void mudaVisibilidade(boolean b) {
        this.janela.setVisible(b);
    }

    @Override
    public void adicionarAcoes() {
        view.adicionarAcaoBtnCadastrar(e -> acaoCadastrarEntidade());
        view.adicionarAcaoBtnVoltar(e -> acaoVoltar());
    }

    @Override
    public void acaoCadastrarEntidade() {
        try {
            this.entidadeModel = this.getDadosEntidadeFromView();
            this.contaModel = this.getDadosContaFromView();
            this.entidadeDao.salvar(entidadeModel);
            this.contaDao.salvar(contaModel);
            this.view.exibirMensagem("Clínica cadastrada com sucesso!");
            this.view.limparCampos();
        } catch (EntidadeException | ContaAcessoException e) {
            view.exibirMensagem(e.getMessage());
        }
    }

    @Override
    public void acaoVoltar() {
        this.view.limparCampos();
        this.navegacao.exibirLogin();
    }

    private IEntidadeModel getDadosEntidadeFromView() throws EntidadeException {
        String nome = view.getNomeEntidade();
        String cnpj = view.getCnpjEntidade().replaceAll("\\D", "");

        if (nome.isBlank() || cnpj.isBlank()) {
            throw new EntidadeException("Nome e CNPJ da clínica devem ser preenchidos.");
        }
        return new EntidadeModel(nome, cnpj);
    }

    private IContaAcessoModel getDadosContaFromView() throws EntidadeException {
        String nomeAdm = view.getNomeAdm();
        String email = view.getEmail();
        String senha = view.getSenha();

        if (nomeAdm.isBlank() || email.isBlank() || senha.isBlank()) {
            throw new EntidadeException("Todos os campos do administrador devem ser preenchidos.");
        }
        if (senha.length() != 8) {
            throw new EntidadeException("A senha deve conter exatamente 8 caracteres.");
        }
        byte[] salt = gerarSalt();
        byte[] senhaHash = gerarSenhaHash(senha, salt);
        return new ContaAcessoModel(nomeAdm, null, this.entidadeModel.getCnpj(), null, email,
                PerfilEnum.ADMINISTRADOR, salt, senhaHash);
    }

    private byte[] gerarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private byte[] gerarSenhaHash(String senha, byte[] salt) throws EntidadeException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new EntidadeException("Erro ao processar a senha.");
        }
    }
}
