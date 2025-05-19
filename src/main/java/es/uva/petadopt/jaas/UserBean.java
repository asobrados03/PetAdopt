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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Clase para eliminar las cuentas y los datos asociados
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named("userBean")
@RequestScoped
public class UserBean {

    @EJB
    private UserEJB userEJB;

    public String deleteAccount() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ec = ctx.getExternalContext();
        String email = ec.getUserPrincipal().getName();

        try {
            userEJB.deleteAccount(email);
            
            ec.getFlash().setKeepMessages(true);
            ctx.addMessage(null, 
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Cuenta eliminada correctamente",
                    "Tu cuenta y datos asociados han sido borrados."
                )
            );
            
            ec.invalidateSession();
            return "/index?faces-redirect=true";
        
        } catch (Exception e) {
            ctx.addMessage(null, 
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al eliminar la cuenta",
                    "No se pudo completar la operación. Contacta al soporte."
                )
            );
            return null;
        }
    }
}
