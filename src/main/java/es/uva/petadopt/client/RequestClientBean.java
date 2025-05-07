/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.client;

import es.uva.petadopt.client.RequestBackingBean;
import es.uva.petadopt.client.PetBackingBean;
import es.uva.petadopt.entities.Adoptionrequests;
import es.uva.petadopt.entities.Pets;
import es.uva.petadopt.json.AdoptionReader;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author vcast
 */
@RequestScoped
@Named
public class RequestClientBean {
    Client client;
    WebTarget target;
    
    @Inject
    RequestBackingBean bean;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.adoptionrequests");
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public Adoptionrequests[] getRequests(int petId) {
        /*return em.createNamedQuery("Pets.findBySpecies", Pets.class)
                .setParameter("email", )
                .getResultList();
        */
        return null;
    }
    
    public Adoptionrequests[] getRequestsByPet(int petId) {
        return target
                .request()
                .get(Adoptionrequests[].class);
    }

    public Adoptionrequests getRequest() {
        return target
                .register(AdoptionReader.class)
                .path("{id}")
                .resolveTemplate("id", bean.getRequestId())
                .request(MediaType.APPLICATION_JSON)
                .get(Adoptionrequests.class);
    }

    public void deleteRequest() {
        target.path("{id}")
                .resolveTemplate("id",
                         bean.getRequestId())
                .request()
                .delete();
    }
}
