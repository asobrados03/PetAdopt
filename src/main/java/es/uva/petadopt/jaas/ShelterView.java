/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Shelters;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author vcast
 */
@Named
@SessionScoped
public class ShelterView implements Serializable{
    
    @EJB
    private UserEJB userEJB;
    
    public Shelters getShelterInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String shelter = request.getUserPrincipal().getName();
       
        Shelters shelterInfo = userEJB.findShelterByEmail(shelter);
        return shelterInfo;
    }  
    
}
