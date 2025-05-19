/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Adoptionrequests;
import es.uva.petadopt.entities.Clients;
import es.uva.petadopt.entities.Pets;
import es.uva.petadopt.entities.Shelters;
import es.uva.petadopt.entities.UserGroups;
import es.uva.petadopt.entities.Users;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Clase para gestionar los usuarios 
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;
    
    public Users createUser(Users user, String role) {
        try {
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
        } catch (Exception e) {
        }
        
        UserGroups group = new UserGroups();
        group.setEmail(user.getEmail());
        group.setGroupname(role);
        
        em.persist(user);
        em.persist(group);
        
        return user;
    }
    
    public Users createClient(Users user, Clients client) {
        createUser(user, "clients");
        em.persist(client);
        return user;
    }
    
    public Users createShelter(Users user, Shelters shelter) {
        createUser(user, "shelters");
        shelter.setAuthorized(false);
        em.persist(shelter);
        return user;
    }
    
    public Users findByEmail(String email) {
        TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.email = :email", Users.class);
        query.setParameter("email", email);
        Users user = null;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
        }
        return user;
    }
    
     public Shelters findShelterByEmail(String email) {
        TypedQuery<Shelters> query = em.createQuery(
                "SELECT s FROM Shelters s WHERE s.email = :email", Shelters.class);
        query.setParameter("email", email);
        Shelters shelter = null;
        try {
            shelter = query.getSingleResult();
        } catch (Exception e) {
        }
        return shelter;
    }
    
    public List<Shelters> getPendingShelters() {
        TypedQuery<Shelters> query = em.createQuery(
                "SELECT s FROM Shelters s WHERE s.authorized = false", Shelters.class);
        return query.getResultList();
    }
    
    public void authorizeShelter(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        if (shelter != null) {
            shelter.setAuthorized(true);
            em.merge(shelter);
        }
    }
    
    public void denyShelter(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        Users user = findByEmail(email);
        
        if (shelter != null && user != null) {
            TypedQuery<UserGroups> query = em.createQuery(
                    "SELECT ug FROM UserGroups ug WHERE ug.email = :email", UserGroups.class);
            query.setParameter("email", email);
            List<UserGroups> groups = query.getResultList();
            
            for (UserGroups group : groups) {
                em.remove(group);
            }
            
            em.remove(shelter);
            em.remove(user);
        }
    }
    
    public boolean isAdult(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
    
    public void deleteAccount(String email) throws Exception {
        Users user = em.find(Users.class, email);
        if (user == null) {
            throw new Exception("Usuario no encontrado");
        }

        Clients client = em.find(Clients.class, email);
        if (client != null) {
            List<Adoptionrequests> solicitudes = em.createQuery(
                "SELECT ar FROM Adoptionrequests ar WHERE ar.clientemail = :email", 
                Adoptionrequests.class)
                .setParameter("email", email)
                .getResultList();
            for (Adoptionrequests sol : solicitudes) {
                em.remove(sol);  
            }

            em.remove(client);
        }

        Shelters shelter = em.find(Shelters.class, email);
        if (shelter != null) {
            List<Pets> mascotas = em.createQuery(
                "SELECT p FROM Pets p WHERE p.shelterEmail = :email", 
                Pets.class)
                .setParameter("email", email)
                .getResultList();
            
            for (Pets pet : mascotas) {
                List<Adoptionrequests> solicitudesPet = em.createQuery(
                    "SELECT ar FROM Adoptionrequests ar WHERE ar.petid = :petId", 
                    Adoptionrequests.class)
                    .setParameter("petId", pet.getId())
                    .getResultList();
                for (Adoptionrequests sol : solicitudesPet) {
                    em.remove(sol);  
                }
                em.remove(pet);  
            }

            em.remove(shelter);
        }

        List<UserGroups> grupos = em.createQuery(
                "SELECT ug FROM UserGroups ug WHERE ug.email = :email", 
                UserGroups.class)
            .setParameter("email", email)
            .getResultList();
        for (UserGroups group : grupos) {
            em.remove(group);
        }

        em.remove(user);
    }
    
    public boolean isShelterAuthorized(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        return shelter != null && shelter.getAuthorized();
    }
}
