/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Users;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Clase de funcionalidades del login
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named
@SessionScoped
public class LoginView implements Serializable {

    @EJB
    private UserEJB userEJB;

    private String email;
    private String password;
    private Users user;

    @Inject
    private SessionBean sessionBean;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(email, password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Login incorrecto!", null));
            return "login";
        }

        this.user = userEJB.findByEmail(request.getUserPrincipal().getName());

        sessionBean.setShelterName(user.getName());

        if (request.isUserInRole("clients")) {
            return "/clients/privatepage?faces-redirect=true";
        } else if (request.isUserInRole("admin")) {
            return "/admin/privatepage?faces-redirect=true";
        } else if (request.isUserInRole("shelters")) {
            return "/shelters/privatepage?faces-redirect=true";
        } else {
            return "login";
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            this.user = null;
            request.logout();
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
            return "/index?faces-redirect=true";
        } catch (ServletException e) {
            return null;
        }
    }

    public String deleteAccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            String currentEmail = request.getUserPrincipal().getName();
            userEJB.deleteAccount(currentEmail);

            request.logout();
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cuenta eliminada correctamente", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al eliminar la cuenta", null));
        }

        return "/index?faces-redirect=true";
    }

    public boolean isUserShelterAuthorized() {
        if (user == null) {
            return false;
        }

        return userEJB.isShelterAuthorized(user.getEmail());
    }

    public Users getAuthenticatedUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
