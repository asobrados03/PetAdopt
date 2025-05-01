/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.client;

import es.uva.petadopt.entities.Pets;
import java.io.Serializable;
import java.math.BigDecimal;
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
    
    private Pets pet = new Pets();
    
    @Inject
    PetClientBean petClientBean;

    public int getPetId() {
        return petId;
    }

    public Pets getPet() {
        return pet;
    }

    public void setPet(Pets pet) {
        this.pet = pet;
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
        petClientBean.updatePet(pet);
        return "pets"; // redirige a la lista
    }
    
}
