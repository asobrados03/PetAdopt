/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Clients;
import es.uva.petadopt.entities.Users;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

/**
 * Clase de funcionalidades del registro de clientes
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named
@SessionScoped
public class RegisterClientView implements Serializable {

    @EJB
    private UserEJB userEJB;

    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmPassword;
    private String nif;
    private String address;
    private String phone;
    private Date birthDate;

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

        UIInput uiInputNIF = (UIInput) components.findComponent("nif");
        String nif = uiInputNIF.getLocalValue() == null ? "" : uiInputNIF.getLocalValue().toString();

        if (nif != null) {
            if (nif.length() == 9) {
                try {
                    int num = Integer.parseInt(nif.substring(0, 8));
                    char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H',
                        'L', 'C', 'K', 'E'};

                    char letter = letras[num % 23];

                    if (letter != nif.charAt(8)) {
                        FacesMessage msg = new FacesMessage("El NIF no existe");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        facesContext.addMessage(uiInputNIF.getClientId(), msg);
                        facesContext.renderResponse();
                    }
                } catch (Exception e) {
                    FacesMessage msg = new FacesMessage("Error de formato");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    facesContext.addMessage(uiInputNIF.getClientId(), msg);
                    facesContext.renderResponse();
                }

            } else {
                FacesMessage msg = new FacesMessage("Error de formato");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesContext.addMessage(uiInputNIF.getClientId(), msg);
                facesContext.renderResponse();
            }

        }

        UIInput uiInputBirthDate = (UIInput) components.findComponent("birthDate");
        Date birthDate = (Date) uiInputBirthDate.getLocalValue();

        if (birthDate != null) {
            LocalDate dob = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!userEJB.isAdult(dob)) {
                FacesMessage msg = new FacesMessage("Debes ser mayor de edad para registrarte");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesContext.addMessage(uiInputBirthDate.getClientId(), msg);
                facesContext.renderResponse();
            }
        }
    }

    public String register() {
        LocalDate dob = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!userEJB.isAdult(dob)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Debes ser mayor de edad para registrarte"));
            return null;
        }

        Users user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        Clients client = new Clients();
        client.setEmail(email);
        client.setSurname(surname);
        client.setNif(nif);
        client.setAddres(address);
        client.setPhone(phone);
        client.setBirthDate(java.sql.Date.valueOf(dob));

        try {
            userEJB.createClient(user, client);
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
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.confirmPassword = null;
        this.nif = null;
        this.address = null;
        this.phone = null;
        this.birthDate = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
