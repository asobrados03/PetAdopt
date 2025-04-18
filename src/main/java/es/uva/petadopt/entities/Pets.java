/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alfre
 */
@Entity
@Table(name = "pets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pets.findAll", query = "SELECT p FROM Pets p"),
    @NamedQuery(name = "Pets.findById", query = "SELECT p FROM Pets p WHERE p.id = :id"),
    @NamedQuery(name = "Pets.findByName", query = "SELECT p FROM Pets p WHERE p.name = :name"),
    @NamedQuery(name = "Pets.findBySpecies", query = "SELECT p FROM Pets p WHERE p.species = :species"),
    @NamedQuery(name = "Pets.findByBreed", query = "SELECT p FROM Pets p WHERE p.breed = :breed"),
    @NamedQuery(name = "Pets.findByAge", query = "SELECT p FROM Pets p WHERE p.age = :age"),
    @NamedQuery(name = "Pets.findByHealtStatus", query = "SELECT p FROM Pets p WHERE p.healtStatus = :healtStatus"),
    @NamedQuery(name = "Pets.findByAdoptionCost", query = "SELECT p FROM Pets p WHERE p.adoptionCost = :adoptionCost"),
    @NamedQuery(name = "Pets.findByShelterName", query = "SELECT p FROM Pets p WHERE p.shelterName = :shelterName"),
    @NamedQuery(name = "Pets.getAllSpecies", query = "SELECT DISTINCT p.species FROM Pets p")})
public class Pets implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "species")
    private String species;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "breed")
    private String breed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "age")
    private int age;
    @Size(max = 250)
    @Column(name = "healt_status")
    private String healtStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adoption_cost")
    private BigInteger adoptionCost;
    @Size(max = 50)
    @Column(name = "shelter_name")
    private String shelterName;

    public Pets() {
    }

    public Pets(Integer id) {
        this.id = id;
    }

    public Pets(Integer id, String name, String species, String breed, int age, BigInteger adoptionCost) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.adoptionCost = adoptionCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHealtStatus() {
        return healtStatus;
    }

    public void setHealtStatus(String healtStatus) {
        this.healtStatus = healtStatus;
    }

    public BigInteger getAdoptionCost() {
        return adoptionCost;
    }

    public void setAdoptionCost(BigInteger adoptionCost) {
        this.adoptionCost = adoptionCost;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pets)) {
            return false;
        }
        Pets other = (Pets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.uva.petadopt.entities.Pets[ id=" + id + " ]";
    }
    
}
