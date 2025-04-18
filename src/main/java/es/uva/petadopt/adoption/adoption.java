/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.adoption;

import es.uva.petadopt.entities.Pets;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 
 */
@Named("adoption")
@FlowScoped("adoption")
public class adoption implements Serializable {
    
    private int petId;
    private String species;

    @PersistenceContext
    private EntityManager em;
    
    private double precio = new Double(0);
    private String tarjeta = "";
    private Date fecha = new Date();
    
    
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
    
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    //TODO: mirar para cambiar el null
    public List<String> getAllSpecies() {
        try {
            List<String> list = em.createNamedQuery("Pets.getAllSpecies", String.class)
                    .getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<Pets> getPetsBySpecies() {
        return em.createNamedQuery("Pets.findBySpecies", Pets.class)
                .setParameter("species", species)
                .getResultList();
    }
    
    public Pets getSelectedPet() {
        try {
            return em.createNamedQuery("Pets.findById", Pets.class)
                    .setParameter("id", petId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
