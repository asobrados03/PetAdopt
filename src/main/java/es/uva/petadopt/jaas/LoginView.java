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
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alfre
 */
@Named
@SessionScoped
public class LoginView implements Serializable {
    
    @EJB
    private UserEJB userEJB;
    
    private String email;
    private String password;
    private Users user;
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            request.login(email, password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Login incorrecto!" + e, null));
            return "login";
        }
        
        this.user = userEJB.findByEmail(request.getUserPrincipal().getName());
        
        if (request.isUserInRole("admin")) {
            return "/admin/dashboard?faces-redirect=true";
        } else if (request.isUserInRole("shelters")) {
            boolean isAuthorized = userEJB.isShelterAuthorized(email);
            return "/shelters/dashboard?faces-redirect=true";
        } else if (request.isUserInRole("clients")) {
            return "/users/dashboard?faces-redirect=true";
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
        } catch (ServletException e) {
            System.out.println("Fallo durante el proceso de logout!");
        }
        
        return "/index?faces-redirect=true";
    }
    
    public String deleteAccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            String currentEmail = request.getUserPrincipal().getName();
            userEJB.deleteAccount(currentEmail);
            
            // Cerrar sesión
            request.logout();
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Cuenta eliminada correctamente", null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al eliminar la cuenta", null));
            e.printStackTrace();
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
    
    // Getters and setters
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
