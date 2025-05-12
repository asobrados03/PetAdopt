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
 *
 * @author alfre
 */
@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;
    
    // Método para crear un usuario general
    public Users createUser(Users user, String role) {
        try {
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        UserGroups group = new UserGroups();
        group.setEmail(user.getEmail());
        group.setGroupname(role);
        
        em.persist(user);
        em.persist(group);
        
        return user;
    }
    
    // Método para crear un cliente
    public Users createClient(Users user, Clients client) {
        createUser(user, "clients");
        em.persist(client);
        return user;
    }
    
    // Método para crear un refugio
    public Users createShelter(Users user, Shelters shelter) {
        createUser(user, "shelters");
        shelter.setAuthorized(false);
        em.persist(shelter);
        return user;
    }
    
    // Método para encontrar un usuario por email
    public Users findByEmail(String email) {
        TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.email = :email", Users.class);
        query.setParameter("email", email);
        Users user = null;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            // No se encontró el usuario
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
            // No se encontró el usuario
        }
        return shelter;
    }
    
    // Método para obtener todos los refugios pendientes de autorización
    public List<Shelters> getPendingShelters() {
        TypedQuery<Shelters> query = em.createQuery(
                "SELECT s FROM Shelters s WHERE s.authorized = false", Shelters.class);
        return query.getResultList();
    }
    
    // Método para autorizar un refugio
    public void authorizeShelter(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        if (shelter != null) {
            shelter.setAuthorized(true);
            em.merge(shelter);
        }
    }
    
    // Método para rechazar un refugio
    public void denyShelter(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        Users user = findByEmail(email);
        
        if (shelter != null && user != null) {
            // Eliminar UserGroups
            TypedQuery<UserGroups> query = em.createQuery(
                    "SELECT ug FROM UserGroups ug WHERE ug.email = :email", UserGroups.class);
            query.setParameter("email", email);
            List<UserGroups> groups = query.getResultList();
            
            for (UserGroups group : groups) {
                em.remove(group);
            }
            
            // Eliminar Shelter y User
            em.remove(shelter);
            em.remove(user);
        }
    }
    
    // Verificar si un cliente es mayor de edad
    public boolean isAdult(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
    
    // Método para eliminar cuenta de usuario
    public void deleteAccount(String email) throws Exception {
        // 1. Buscar el usuario y verificar su existencia
        Users user = em.find(Users.class, email);
        if (user == null) {
            throw new Exception("Usuario no encontrado");
        }

        // 2a. Si es cliente, eliminar sus solicitudes de adopción
        Clients client = em.find(Clients.class, email);
        if (client != null) {
            // Buscar todas las solicitudes de adopción de este cliente
            List<Adoptionrequests> solicitudes = em.createQuery(
                "SELECT ar FROM Adoptionrequests ar WHERE ar.clientemail = :email", 
                Adoptionrequests.class)
                .setParameter("email", email)
                .getResultList();
            for (Adoptionrequests sol : solicitudes) {
                em.remove(sol);  // eliminar cada solicitud de adopción
            }
            // Eliminar el registro de cliente
            em.remove(client);
        }

        // 2b. Si es refugio, eliminar sus mascotas y solicitudes asociadas
        Shelters shelter = em.find(Shelters.class, email);
        if (shelter != null) {
            // Buscar todas las mascotas registradas por este refugio
            List<Pets> mascotas = em.createQuery(
                "SELECT p FROM Pets p WHERE p.shelterEmail = :email", 
                Pets.class)
                .setParameter("email", email)
                .getResultList();
            for (Pets pet : mascotas) {
                // Por cada mascota, eliminar primero las solicitudes de adopción asociadas
                List<Adoptionrequests> solicitudesPet = em.createQuery(
                    "SELECT ar FROM Adoptionrequests ar WHERE ar.petid = :petId", 
                    Adoptionrequests.class)
                    .setParameter("petId", pet.getId())
                    .getResultList();
                for (Adoptionrequests sol : solicitudesPet) {
                    em.remove(sol);  // eliminar solicitud asociada a la mascota
                }
                em.remove(pet);  // eliminar la mascota
            }
            // Eliminar el registro de refugio
            em.remove(shelter);
        }

        // 3. Eliminar los grupos/roles del usuario (ej. entradas en UserGroups)
        List<UserGroups> grupos = em.createQuery(
                "SELECT ug FROM UserGroups ug WHERE ug.email = :email", 
                UserGroups.class)
            .setParameter("email", email)
            .getResultList();
        for (UserGroups group : grupos) {
            em.remove(group);
        }

        // 4. Eliminar la cuenta de usuario principal
        em.remove(user);
    }
    
    // Método para verificar si un refugio está autorizado
    public boolean isShelterAuthorized(String email) {
        Shelters shelter = em.find(Shelters.class, email);
        return shelter != null && shelter.getAuthorized();
    }
}
