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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
    WebTarget target2;

    @Inject
    RequestBackingBean bean;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.adoptionrequests");
        target2 = client.target("http://localhost:8080/PetAdopt/webresources/es.uva.petadopt.entities.pets");
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public Pets setPetsFromEmail(String email) {
        return target.register(AdoptionReader.class)
                .path("{petId}")
                .resolveTemplate("shelterEmail", email)
                .request(MediaType.APPLICATION_JSON)
                .get(Pets.class);
    }

    public List<Pets> getPets() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String prop = request.getUserPrincipal().getName();
        System.out.println("propietario: " + prop);
        Pets[] pets = target2
                .request(MediaType.APPLICATION_JSON)
                .get(Pets[].class);

        List<Pets> petsF = new ArrayList<>();

        for (Pets pet : pets) {
            System.out.println("pet " + pet.getId());
            if (pet.getShelterEmail() != null && pet.getShelterEmail().equalsIgnoreCase(prop)) {
                
                petsF.add(pet);
            }
        }

        return petsF;
    }

    public Adoptionrequests[] getRequests() {
        return target
                .request()
                .get(Adoptionrequests[].class);
    }

    public List<Adoptionrequests> getRequestsByShelter() {

        List<Adoptionrequests> requests = new ArrayList<>();
        Adoptionrequests[] allRequests = getRequests();
        List<Pets> pets = getPets();

        if (pets != null && allRequests != null) {
            for (Pets pet : pets) {
                for (Adoptionrequests request : allRequests) {
                    if (pet.getId() == request.getPetid()) {
                        System.out.println("print" + request.getId());
                        requests.add(request);
                    }
                }
            }
        }

        return requests;
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
