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
import javax.faces.context.Flash;
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

    public void deleteAccount() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ec = ctx.getExternalContext();
        String email = ec.getUserPrincipal().getName();

        try {
            // 1. Eliminación de datos en cascada
            userEJB.deleteAccount(email);

            // 2. Preparar mensaje para la siguiente vista
            Flash flash = ec.getFlash();
            flash.setKeepMessages(true);   // conservar el FacesMessage
            flash.setRedirect(true);       // indicamos que habrá redirect
            ctx.addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Cuenta eliminada",
                    "Tu cuenta y datos asociados han sido borrados."
                )
            );

            // 3. Invalidar sesión (ahora que Flash ya está listo)
            ec.invalidateSession();

            // 4. Redirigir manualmente al index.xhtml
            String ctxPath = ec.getRequestContextPath();
            ec.redirect(ctxPath + "/index.xhtml");

            // 5. Marcar la respuesta como completa
            ctx.responseComplete();

        } catch (Exception e) {
            // Si algo falla, mostramos error en el mismo diálogo
            ctx.addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al eliminar la cuenta",
                    "No se pudo completar la operación. Contacta al soporte."
                )
            );
            // No hacemos redirect; el diálogo se quedará ahí y mostrará el error
        }
    }
}
