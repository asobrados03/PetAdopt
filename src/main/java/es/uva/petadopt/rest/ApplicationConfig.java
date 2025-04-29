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
 * @author alfre
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(es.uva.petadopt.rest.AdoptionRequestsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.PetsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.SheltersFacadeREST.class);
        resources.add(es.uva.petadopt.rest.UserGroupsFacadeREST.class);
        resources.add(es.uva.petadopt.rest.UsersFacadeREST.class);
    }
    
}
