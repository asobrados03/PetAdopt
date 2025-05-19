/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(es.uva.petadopt.json.AdoptionReader.class);
        resources.add(es.uva.petadopt.json.AdoptionWriter.class);
        resources.add(es.uva.petadopt.json.PetReader.class);
        resources.add(es.uva.petadopt.json.PetWriter.class);
        resources.add(es.uva.petadopt.rest.AdoptionrequestsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.ClientsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.PetsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.SheltersFacadeREST.class);
        resources.add(es.uva.petadopt.rest.UserGroupsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.UsersFacadeREST.class);
    }

}
