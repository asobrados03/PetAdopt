/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.client;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alfre
 */
@Stateless
public class ShelterServiceEJB {
    
    @PersistenceContext
    private EntityManager em;

    public String findShelterNameByUsername(String username) {
        // Consulta para obtener el nombre del refugio desde BD
        return em.createQuery(
            "SELECT s.name FROM User u JOIN u.shelter s WHERE u.username = :username", 
            String.class
        )
        .setParameter("username", username)
        .getSingleResult();
    }
}
