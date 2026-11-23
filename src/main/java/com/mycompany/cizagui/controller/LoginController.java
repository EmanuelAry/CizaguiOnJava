package com.mycompany.cizagui.controller;


import com.mycompany.cizagui.contracts.controller.ILoginController;
import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;
import com.mycompany.cizagui.contracts.dao.IEntidadeDao;
import com.mycompany.cizagui.contracts.model.IContaAcessoModel;
import com.mycompany.cizagui.exeptions.LoginException;
import com.mycompany.cizagui.model.SessaoUsuario;
import com.mycompany.cizagui.view.LoginView;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ary
 */
public class LoginController implements ILoginController {
    private LoginView loginView;
    private SessaoUsuario sessao;
    private IContaAcessoDao contaDao;
    private IEntidadeDao entidadeDao;
    private INavegacao navegacao;

    public LoginController(IContaAcessoDao contaDao, IEntidadeDao entidadeDao, INavegacao navegacao){
        this.loginView = new LoginView();
        this.contaDao = contaDao;
        this.entidadeDao = entidadeDao;
        this.navegacao = navegacao;
        this.adicionarAcoes();
    }

    public SessaoUsuario getSessao() {
        return sessao;
    }

    @Override
    public void adicionarAcoes(){
        loginView.adicionarAcaoBtnEntrar(e -> acaoEntrar());
        loginView.adicionarAcaoBtnNovaEntidade(e -> acaoNovaEntidade());
    }

    @Override
    public void acaoEntrar(){
        try{
            IContaAcessoModel conta = this.getDadosContaFromView();
            this.validarSenha(conta, loginView.getSenha());
            this.sessao = conta.abrirSessao();
            this.loginView.exibirMensagem("Bem-vindo(a), " + conta.getNome() + "!");
            this.loginView.limparCampos();
            this.navegacao.exibirTelaPrincipal(this.sessao);
        }catch(LoginException e){
            loginView.exibirMensagem(e.getMessage());
        }
    }

    @Override
    public void acaoNovaEntidade(){
        this.loginView.limparCampos();
        this.navegacao.exibirCadastroEntidade();
    }

    @Override
    public void mudaVisibilidade(boolean b){
        this.loginView.setVisible(b);
    }

    private IContaAcessoModel getDadosContaFromView() throws LoginException {
        String cnpj = loginView.getCnpj().replaceAll("\\D", "");
        String email = loginView.getEmail();
        String senha = loginView.getSenha();

        if (cnpj.isBlank() || email.isBlank() || senha.isBlank()) {
            throw new LoginException("Todos os campos de login devem ser preenchidos.");
        }
        entidadeDao.buscarPorCnpj(cnpj)
                .orElseThrow(() -> new LoginException("Clínica não encontrada para o CNPJ informado."));
        return contaDao.buscarPorCnpjEmail(cnpj, email)
                .orElseThrow(() -> new LoginException("Usuário não encontrado para o e-mail informado."));
    }

    private void validarSenha(IContaAcessoModel conta, String senha) throws LoginException {
        byte[] senhaHash = gerarSenhaHash(senha, conta.getSalt());
        if (!MessageDigest.isEqual(conta.getSenhaHash(), senhaHash)) {
            throw new LoginException("Senha incorreta.");
        }
    }

    private byte[] gerarSenhaHash(String senha, byte[] salt) throws LoginException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new LoginException("Erro ao processar a senha.");
        }
    }
}
