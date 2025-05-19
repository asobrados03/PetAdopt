/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.chat;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Clase para redirigir al salir del chat
 * 
 * @authors: Víctor Castrillo y Alfredo Sobrados  
 */

@Named
@SessionScoped
public class ChatBean implements Serializable {

    public String redirectByRole() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getExternalContext().isUserInRole("admin")) {
            return "/admin/home.xhtml?faces-redirect=true";
        } else if (context.getExternalContext().isUserInRole("user")) {
            return "/user/home.xhtml?faces-redirect=true";
        } else {
            return "/regok.xhtml?faces-redirect=true";
        }
    }
}
