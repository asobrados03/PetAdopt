/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alfre
 */
@Entity
@Table(name = "adoption_requests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdoptionRequests.findAll", query = "SELECT a FROM AdoptionRequests a"),
    @NamedQuery(name = "AdoptionRequests.findById", query = "SELECT a FROM AdoptionRequests a WHERE a.id = :id"),
    @NamedQuery(name = "AdoptionRequests.findByClientEmail", query = "SELECT a FROM AdoptionRequests a WHERE a.clientEmail = :clientEmail"),
    @NamedQuery(name = "AdoptionRequests.findByPetId", query = "SELECT a FROM AdoptionRequests a WHERE a.petId = :petId"),
    @NamedQuery(name = "AdoptionRequests.findByRequestDate", query = "SELECT a FROM AdoptionRequests a WHERE a.requestDate = :requestDate"),
    @NamedQuery(name = "AdoptionRequests.findByStatus", query = "SELECT a FROM AdoptionRequests a WHERE a.status = :status")})
public class AdoptionRequests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "client_email")
    private String clientEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pet_id")
    private int petId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "request_date")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @Size(max = 2147483647)
    @Column(name = "status")
    private String status;

    public AdoptionRequests() {
    }

    public AdoptionRequests(Integer id) {
        this.id = id;
    }

    public AdoptionRequests(Integer id, String clientEmail, int petId, Date requestDate) {
        this.id = id;
        this.clientEmail = clientEmail;
        this.petId = petId;
        this.requestDate = requestDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof AdoptionRequests)) {
            return false;
        }
        AdoptionRequests other = (AdoptionRequests) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.uva.petadopt.entities.AdoptionRequests[ id=" + id + " ]";
    }
    
}
