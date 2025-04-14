/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "shelters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Shelters.findAll", query = "SELECT s FROM Shelters s"),
    @NamedQuery(name = "Shelters.findByEmail", query = "SELECT s FROM Shelters s WHERE s.email = :email"),
    @NamedQuery(name = "Shelters.findByCif", query = "SELECT s FROM Shelters s WHERE s.cif = :cif"),
    @NamedQuery(name = "Shelters.findByAddres", query = "SELECT s FROM Shelters s WHERE s.addres = :addres"),
    @NamedQuery(name = "Shelters.findByPhone", query = "SELECT s FROM Shelters s WHERE s.phone = :phone"),
    @NamedQuery(name = "Shelters.findByShelterName", query = "SELECT s FROM Shelters s WHERE s.shelterName = :shelterName"),
    @NamedQuery(name = "Shelters.findByAuthorized", query = "SELECT s FROM Shelters s WHERE s.authorized = :authorized")})
public class Shelters implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "cif")
    private String cif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "addres")
    private String addres;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "shelter_name")
    private String shelterName;
    @Column(name = "authorized")
    private Boolean authorized;

    public Shelters() {
    }

    public Shelters(String email) {
        this.email = email;
    }

    public Shelters(String email, String cif, String addres, String phone, String shelterName) {
        this.email = email;
        this.cif = cif;
        this.addres = addres;
        this.phone = phone;
        this.shelterName = shelterName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shelters)) {
            return false;
        }
        Shelters other = (Shelters) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.uva.petadopt.entities.Shelters[ email=" + email + " ]";
    }
    
}
