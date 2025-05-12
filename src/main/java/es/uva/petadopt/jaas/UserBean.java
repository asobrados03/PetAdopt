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
 *
 * @author alfre
 */
@Named("userBean")
@RequestScoped
public class UserBean {

    @EJB
    private UserEJB userEJB;

    /**
     * Elimina la cuenta y datos asociados, añade un mensaje en flash
     * y hace un PRG a index.xhtml.
     */
    public String deleteAccount() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ec = ctx.getExternalContext();
        String email = ec.getUserPrincipal().getName();

        try {
            // 1) Eliminación transaccional en el EJB
            userEJB.deleteAccount(email);

            // 2) Conservar mensajes tras redirect
            ec.getFlash().setKeepMessages(true);
            ctx.addMessage(null, 
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Cuenta eliminada correctamente",
                    "Tu cuenta y datos asociados han sido borrados."
                )
            );
            
            // 3) Logout (quita credenciales)
            ec.invalidateSession();

            // 4) Navegación Post-Redirect-Get
            return "/index?faces-redirect=true";
        }
        catch (Exception e) {
            // Si algo falla, mostramos error y nos quedamos en la misma vista
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
