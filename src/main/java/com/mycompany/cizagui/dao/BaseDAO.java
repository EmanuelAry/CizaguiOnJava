/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cizagui.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * @author ary
 */
public abstract class BaseDAO {


    protected static final String UNIDADE_PERSISTENCIA = "cizagui";

    private static final String[] CONSTRAINTS_AUXILIARES = {
        "CREATE UNIQUE INDEX IF NOT EXISTS uk_conta_acesso_cnpj_email ON conta_acesso (cnpj, email)",
    };


    protected static EntityManagerFactory emf;

    protected EntityManager em;

    public BaseDAO() {
        abrirConexao();
    }

    protected synchronized void abrirConexao() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(UNIDADE_PERSISTENCIA);
            aplicarConstraintsAuxiliares(emf);
        }
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
        }
    }

    protected void fecharConexao() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    protected void desfazer() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }


    protected <T> T exigir(Object valor, Class<T> tipo) {
        if (tipo.isInstance(valor)) {
            return tipo.cast(valor);
        }
        throw new IllegalArgumentException("Esperado " + tipo.getSimpleName()
                + ", recebido: " + (valor == null ? "null" : valor.getClass().getName()));
    }

    private static void aplicarConstraintsAuxiliares(EntityManagerFactory fabrica) {
        EntityManager auxiliar = fabrica.createEntityManager();
        try {
            auxiliar.getTransaction().begin();
            for (String sql : CONSTRAINTS_AUXILIARES) {
                auxiliar.createNativeQuery(sql).executeUpdate();
            }
            auxiliar.getTransaction().commit();
        } finally {
            auxiliar.close();
        }
    }
}
