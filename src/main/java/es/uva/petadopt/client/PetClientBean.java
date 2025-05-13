/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.client;

import es.uva.petadopt.entities.Adoptionrequests;
import es.uva.petadopt.entities.Pets;
import es.uva.petadopt.jaas.SessionBean;
import es.uva.petadopt.json.PetReader;
import es.uva.petadopt.json.PetWriter;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author alfre
 */
@Named
@RequestScoped
public class PetClientBean {
    Client client;
    WebTarget target;

    @Inject
    private PetBackingBean bean;
    @Inject 
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.pets");
    }
    
    @PreDestroy
    public void destroy() {
        client.close();
    }

    public Pets[] getPets() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String prop = request.getUserPrincipal().getName();
        
        return target.queryParam("shelter", request.getUserPrincipal().getName())
            .request(MediaType.APPLICATION_JSON)
            .get(Pets[].class);
    }
    
    public Pets getPet() {
        return target.register(PetReader.class)
                .path("{petId}")
                .resolveTemplate("petId", bean.getPetId())
                .request(MediaType.APPLICATION_JSON)
                .get(Pets.class);
    }

    public void addPet() {
        Pets p = new Pets();
        p.setId(1);
        p.setName(bean.getPetName());
        p.setSpecies(bean.getSpecies());
        p.setBreed(bean.getBreed());
        p.setAge(bean.getAge());
        p.setHealthStatus(bean.getHealthStatus());
        p.setAdoptionCost(bean.getAdoptionCost());
        p.setShelterEmail(bean.getShelterEmail());
        
        target.register(PetWriter.class)
                .request()
                .post(Entity.entity(p, MediaType.APPLICATION_JSON));
    }

    public void updatePet(Pets pet) {
        target.register(PetWriter.class)
                .path("{id}")
                .resolveTemplate("id", pet.getId())
                .request()
                .put(Entity.entity(pet, MediaType.APPLICATION_JSON));
    }

    public void deletePet() {
        target.path("{petId}")
                .resolveTemplate("petId", bean.getPetId())
                .request()
                .delete();
    }
    
    public void deletePetById(int id) {
        target.path("{petId}")
                .resolveTemplate("petId", id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }
}
