/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.client;

import es.uva.petadopt.entities.Pets;
import es.uva.petadopt.jaas.LoginView;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author alfre
 */
@Named
@SessionScoped
public class PetBackingBean implements Serializable {

    int petId;
    String petName;
    String species;
    String breed;
    int age;
    String healthStatus;
    BigDecimal adoptionCost;
    String shelterEmail;

    @Inject
    PetClientBean petClientBean;

    @Inject
    private LoginView loginView;

    @PostConstruct
    public void init() {
        // Asigna el nombre del refugio si el usuario es un refugio
        if (loginView.getAuthenticatedUser() != null
                && loginView.getAuthenticatedUser().getName() != null) {
            shelterEmail = loginView.getAuthenticatedUser().getName();
        }
    }

    public String getShelterEmail() {
        return shelterEmail;
    }

    public void setShelterEmail(String shelterEmail) {
        this.shelterEmail = shelterEmail;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public BigDecimal getAdoptionCost() {
        return adoptionCost;
    }

    public void setAdoptionCost(BigDecimal adoptionCost) {
        this.adoptionCost = adoptionCost;
    }

    public int getPetId() {
        return petId;
    }

    public void loadPet() {
        Pets pet = petClientBean.getPet(); // usa el petId actual
        this.petName = pet.getName();
        this.species = pet.getSpecies();
        this.breed = pet.getBreed();
        this.age = pet.getAge();
        this.healthStatus = pet.getHealthStatus();
        this.adoptionCost = pet.getAdoptionCost();
        this.shelterEmail = pet.getShelterEmail();
    }

    public PetClientBean getPetClientBean() {
        return petClientBean;
    }

    public void setPetClientBean(PetClientBean petClientBean) {
        this.petClientBean = petClientBean;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
    
    public void cleanFields(){
        this.petName = null;
        this.species = null;
        this.breed = null;
        this.age = 0;
        this.healthStatus = null;
        this.adoptionCost = null;
        this.shelterEmail = null;
    }

    public String saveChanges() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        // Guardar el mensaje en Flash para que sobreviva al redirect
        ctx.getExternalContext()
                .getFlash()
                .setKeepMessages(true);

        try {
            // Construye y envía el PUT
            Pets p = new Pets();
            p.setId(this.petId);
            p.setName(this.petName);
            p.setSpecies(this.species);
            p.setBreed(this.breed);
            p.setAge(this.age);
            p.setHealthStatus(this.healthStatus);
            p.setAdoptionCost(this.adoptionCost);
            p.setShelterEmail(this.shelterEmail);

            petClientBean.updatePet(p);

            // Mensaje de éxito
            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Mascota actualizada",
                    "Los cambios se han guardado correctamente."));
            // Redirige a la lista
            return "pets?faces-redirect=true";
        } catch (Exception e) {
            // Mensaje de error
            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al actualizar",
                    e.getMessage()));
            // Queda en la misma página para que veas el growl
            return null;
        }
    }

}
