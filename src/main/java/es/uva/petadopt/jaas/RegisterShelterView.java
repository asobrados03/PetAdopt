/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Shelters;
import es.uva.petadopt.entities.Users;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

/**
 * Clase de funcionalidades del registro de refugios
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named
@SessionScoped
public class RegisterShelterView implements Serializable {
    
    @EJB
    private UserEJB userEJB;
    
    private String shelterName;
    private String email;
    private String password;
    private String confirmPassword;
    private String cif;
    private String address;
    private String phone;
    
    public void validatePassword(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String password = uiInputPassword.getLocalValue() == null ? "" 
                : uiInputPassword.getLocalValue().toString();
        
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? "" 
                : uiInputConfirmPassword.getLocalValue().toString();
        
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage("Las contraseñas no coinciden");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(uiInputPassword.getClientId(), msg);
            facesContext.renderResponse();
        }
        
        UIInput uiInputEmail = (UIInput) components.findComponent("email");
        String email = uiInputEmail.getLocalValue() == null ? "" 
                : uiInputEmail.getLocalValue().toString();
        
        if (userEJB.findByEmail(email) != null) {
            FacesMessage msg = new FacesMessage("Ya existe un usuario con ese email");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(uiInputEmail.getClientId(), msg);
            facesContext.renderResponse();
        }
    }
    
    public String register() {
        Users user = new Users();
        user.setEmail(email);
        user.setName(shelterName);
        user.setPassword(password);
        
        Shelters shelter = new Shelters();
        shelter.setEmail(email);
        shelter.setCif(cif);
        shelter.setAddres(address);
        shelter.setPhone(phone);
        shelter.setShelterName(shelterName);
        
        try {
            userEJB.createShelter(user, shelter);
            resetFields();
            return "regok";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "Error", "No se pudo completar el registro"));
            return null;
        }
    }
    
    private void resetFields() {
        this.shelterName = null;
        this.email = null;
        this.password = null;
        this.confirmPassword = null;
        this.cif = null;
        this.address = null;
        this.phone = null;
    }
    
    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
