/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import es.uva.petadopt.entities.Shelters;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Clase de funcionalidades para los admin
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named
@SessionScoped
public class AdminView implements Serializable {

    @EJB
    private UserEJB userEJB;

    private List<Shelters> pendingShelters;
    private Shelters selectedShelter;

    @PostConstruct
    public void init() {
        loadPendingShelters();
    }

    public void loadPendingShelters() {
        pendingShelters = userEJB.getPendingShelters();
    }

    public String authorizeShelter(String email) {
        try {
            userEJB.authorizeShelter(email);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito", "Refugio autorizado correctamente"));

            loadPendingShelters();

            return "authorizeShelters?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo autorizar el refugio"));
        }

        return null;
    }

    public String denyShelter(String email) {
        try {
            userEJB.denyShelter(email);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito", "Refugio rechazado correctamente"));
            loadPendingShelters();
            return "authorizeShelters?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo rechazar el refugio"));
        }

        return null;
    }

    public Shelters getShelterByEmail(String email) {
        Shelters shelter = userEJB.findShelterByEmail(email);
        return shelter;
    }

    public String goToShelterInfo(String email) {
        selectedShelter = getShelterByEmail(email);
        return "shelterInfo?faces-redirect=true";
    }

    public List<Shelters> getPendingShelters() {
        return pendingShelters;
    }

    public Shelters getSelectedShelter() {
        return selectedShelter;
    }

    public void setSelectedShelter(Shelters selectedShelter) {
        this.selectedShelter = selectedShelter;
    }
}
