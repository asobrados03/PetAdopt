/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.adoption;

import es.uva.petadopt.entities.Adoptionrequests;
import es.uva.petadopt.entities.Pets;
import es.uva.petadopt.json.AdoptionWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Clase para controlar el flow de adoption
 * 
 * @authors: Víctor Castrillo y Alfredo Sobrados  
 */
@Named
@FlowScoped("adoption")
public class Adoption implements Serializable {
    
    private int petId;
    private String species;

    @PersistenceContext
    private EntityManager em;
    
    private double precio = new Double(0);
    private String tarjeta = "";
    private Date fecha = new Date();
    
    Client client;
    WebTarget target;
    
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
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.adoptionrequests");
    }
    
    @PreDestroy
    public void destroy() {
        client.close();
    }
    
    public void addRequest() {
        Adoptionrequests ar = new Adoptionrequests();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String client = request.getUserPrincipal().getName();
        
        try {   
        LocalDate actual = LocalDate.now();
        
        ar.setClientemail(client);
        ar.setPetid(petId);
        ar.setRequestdate(java.sql.Date.valueOf(actual));
        ar.setPetstatus("pendiente");
     
        
        target.register(AdoptionWriter.class)
              .request()
              .post(Entity.entity(ar, MediaType.APPLICATION_JSON));

        } catch(Exception ex){
            System.out.println("error: " + ex);
        }
    }
}
