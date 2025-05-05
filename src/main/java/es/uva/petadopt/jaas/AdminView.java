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
 *
 * @author alfre
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
        // Autorizar el refugio
        userEJB.authorizeShelter(email);

        // Agregar un mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Éxito", "Refugio autorizado correctamente"));

        // Volver a cargar los refugios pendientes
        loadPendingShelters();

        // Redirigir a la página de refugios autorizados
        return "authorizeShelters?faces-redirect=true"; // Asegúrate de que 'pendingShelters.xhtml' sea el nombre correcto

    } catch (Exception e) {
        // Mostrar mensaje de error si no se puede autorizar el refugio
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No se pudo autorizar el refugio"));
        e.printStackTrace();
    }

    // Si algo falla, no redirige y permanece en la misma página
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
            e.printStackTrace();
        }

        return null;
    }

    public Shelters getShelterByEmail(String email) {
        Shelters shelter = userEJB.findShelterByEmail(email);
        return shelter;
    }

    public String goToShelterInfo(String email) {
        selectedShelter = getShelterByEmail(email);
        return "shelterInfo?faces-redirect=true"; // Navega a shelterInfo.xhtml
    }
    

    // Getters and setters
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
