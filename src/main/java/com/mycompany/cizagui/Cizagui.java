/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cizagui;

import com.mycompany.cizagui.contracts.controller.INavegacao;
import com.mycompany.cizagui.contracts.dao.IAtendimentoDao;
import com.mycompany.cizagui.contracts.dao.IClienteDao;
import com.mycompany.cizagui.contracts.dao.IContaAcessoDao;
import com.mycompany.cizagui.contracts.dao.IEntidadeDao;
import com.mycompany.cizagui.contracts.dao.IProntuarioDao;
import com.mycompany.cizagui.controller.Navegacao;
import com.mycompany.cizagui.dao.AtendimentoDao;
import com.mycompany.cizagui.dao.ClienteDao;
import com.mycompany.cizagui.dao.ContaAcessoDao;
import com.mycompany.cizagui.dao.EntidadeDao;
import com.mycompany.cizagui.dao.ProntuarioDao;

/**
 *
 * @author ary
 */
public class Cizagui {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Cizagui.class.getName());

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Persistência em banco SQLite via JPA (Hibernate). Os DAOs em memória
           continuam disponíveis para testes, mas não são usados aqui. */
        IEntidadeDao entidadeDao = new EntidadeDao();
        IContaAcessoDao contaDao = new ContaAcessoDao();
        IAtendimentoDao atendimentoDao = new AtendimentoDao();
        IClienteDao clienteDao = new ClienteDao();
        IProntuarioDao prontuarioDao = new ProntuarioDao();

        INavegacao navegacao = new Navegacao(entidadeDao, contaDao, atendimentoDao, clienteDao, prontuarioDao);

        /* A primeira tela exibida é a tela de login */
        java.awt.EventQueue.invokeLater(navegacao::exibirLogin);
    }
}
