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
    String health_status;
    BigDecimal adoption_cost;
    String shelter_name;
    
    @Inject
    PetClientBean petClientBean;

    @Inject
    private LoginView loginView;

    @PostConstruct
    public void init() {
        // Asigna el nombre del refugio si el usuario es un refugio
        if (loginView.getAuthenticatedUser() != null 
            && loginView.getAuthenticatedUser().getName() != null) {
            shelter_name = loginView.getAuthenticatedUser().getName();
        }
    }

    public String getShelter_name() {
        return shelter_name;
    }

    public void setShelter_name(String shelter_name) {
        this.shelter_name = shelter_name;
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

    public String getHealth_status() {
        return health_status;
    }

    public void setHealth_status(String health_status) {
        this.health_status = health_status;
    }

    public BigDecimal getAdoption_cost() {
        return adoption_cost;
    }

    public void setAdoption_cost(BigDecimal adoption_cost) {
        this.adoption_cost = adoption_cost;
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
        this.health_status = pet.getHealthStatus();
        this.adoption_cost = pet.getAdoptionCost();
        this.shelter_name = pet.getShelterName();
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
    
    public String saveChanges() {
        Pets p = new Pets();
        p.setId(this.petId);
        p.setName(this.petName);
        p.setSpecies(this.species);
        p.setBreed(this.breed);
        p.setAge(this.age);
        p.setHealthStatus(this.health_status);
        p.setAdoptionCost(this.adoption_cost);
        p.setShelterName(this.shelter_name); // importante para validación en servidor

        petClientBean.updatePet(p);
        return "pets"; // vuelve a la lista
    }

}
