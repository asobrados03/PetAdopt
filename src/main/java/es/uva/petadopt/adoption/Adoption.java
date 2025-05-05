/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.adoption;

import es.uva.petadopt.entities.AdoptionRequests;
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
 *
 * @author 
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
    private String propietario;
    
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
    
    public void setPropietario() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String prop = request.getUserPrincipal().getName();
        this.propietario = prop;
    }
    
    Client client;
    WebTarget target;
    
     @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        //target = client.target("http://localhost:8080/pseFinalSalones/webresources/com.pse.psefinalsalones.entities.espacio");
        target = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.adoptionrequests");
    }
    
    @PreDestroy
    public void destroy() {
        client.close();
    }
    
     
     
    public void addRequest() {
        System.out.println("patata  con");
        AdoptionRequests ar = new AdoptionRequests();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String prop = request.getUserPrincipal().getName();
        try {
            
        LocalDate actual = LocalDate.now();
        
        
       
        
        
        ar.setClientEmail(prop);
        ar.setPetId(petId);
        ar.setRequestDate(java.sql.Date.valueOf(actual));
        
        System.out.println("ID: " + ar.getPetId());
        System.out.println("email: " + ar.getClientEmail());
        System.out.println("fechaActual" + ar.getRequestDate());
        System.out.println("estatus: " + ar.getStatus());
        
        target.register(AdoptionWriter.class)
              .request()
              .post(Entity.entity(ar, MediaType.APPLICATION_JSON));

        
        }catch(Exception ex){
            System.out.println("error: " + ex);
        }
        
        
    }
}
