/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import es.uva.petadopt.jaas.UserEJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alfre
 */
@Named("userBean")
@RequestScoped
public class UserBean {

    @EJB
    private UserEJB userEJB;

    public String delete() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String email = request.getUserPrincipal().getName();

        try {
            userEJB.deleteAccount(email);
            return "/index?faces-redirect=true";
        } catch(Exception e) {
            return null;
        }
        
    }
}
