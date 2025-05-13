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
import es.uva.petadopt.json.AdoptionWriter;
import es.uva.petadopt.json.PetWriter;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.shaded.json.JSONObject;

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
        String api = "http://serpis.infor.uva.es/darklist/api/validar_adoptante/";

        if (pets != null && allRequests != null) {
            for (Pets pet : pets) {
                for (Adoptionrequests request : allRequests) {
                    if (request.getPetstatus().equals("pendiente")) {
                        if (pet.getId() == request.getPetid()) {
                            request.setEnListaNegra(false);
                            Client apiClient = ClientBuilder.newClient();
                            WebTarget apiTarget = apiClient.target(api + request.getClientemail());
                            Response apiResponse = apiTarget.request(MediaType.APPLICATION_JSON).get();

                            //comprobar si esta en la lista
                            if (apiResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                                JSONObject jsonResponse = new JSONObject(apiResponse.readEntity(String.class));
                                apiClient.close();

                                // si esta comprueba el valor de lista negra
                                if (jsonResponse.getString("listaNegra").equals("no")) {
                                    // si es no lo añade
                                    requests.add(request);
                                } else {
                                    // añadir pero solo se muestra el boton de eliminar
                                    request.setEnListaNegra(true);
                                    requests.add(request);
                                }
                            } else {
                                //si no esta lo añade
                                apiClient.close();
                                requests.add(request);
                            }
                            System.out.println("print" + request.getId());

                        }
                    }
                }
            }
        }

        return requests;
    }

    public Adoptionrequests getRequest(int id) {
        return target
                .register(AdoptionReader.class)
                .path("{id}")
                .resolveTemplate("id", id)
                .request(MediaType.APPLICATION_JSON)
                .get(Adoptionrequests.class);
    }

    public String deleteRequest(int id) {
        try {
            target.path("{id}")
                    .resolveTemplate("id", id)
                    .request()
                    .delete();
            return "/shelters/showRequests?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public String rejectRequest(int id) {
        try {
            Adoptionrequests r = getRequest(id);

            r.setId(id);
            r.setPetstatus("rechazada");

            target.register(AdoptionWriter.class)
                    .path("{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(r, MediaType.APPLICATION_JSON));
            return "/shelters/showRequests?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public void rejectRequestNoRedirect(int id) {
        Adoptionrequests r = getRequest(id);

        r.setId(id);
        r.setPetstatus("rechazada");

        target.register(AdoptionWriter.class)
                .path("{id}")
                .resolveTemplate("id", id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(r, MediaType.APPLICATION_JSON));
    }

    public String aceptRequest(int id) {
        try {
            Adoptionrequests r = getRequest(id);

            Adoptionrequests[] allRequests = getRequests();
            for (Adoptionrequests request : allRequests) {
                if (request.getPetid() == r.getPetid()) {
                    rejectRequestNoRedirect(request.getId());
                }
            }

            r.setId(id);
            r.setPetstatus("aceptada");

            target.register(AdoptionWriter.class)
                    .path("{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(r, MediaType.APPLICATION_JSON));
            return "/shelters/showRequests?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
}
