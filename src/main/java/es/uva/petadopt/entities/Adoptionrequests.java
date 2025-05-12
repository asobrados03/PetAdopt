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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vcast
 */
@Entity
@Table(name = "adoptionrequests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adoptionrequests.findAll", query = "SELECT a FROM Adoptionrequests a"),
    @NamedQuery(name = "Adoptionrequests.findById", query = "SELECT a FROM Adoptionrequests a WHERE a.id = :id"),
    @NamedQuery(name = "Adoptionrequests.findByClientemail", query = "SELECT a FROM Adoptionrequests a WHERE a.clientemail = :clientemail"),
    @NamedQuery(name = "Adoptionrequests.findByPetid", query = "SELECT a FROM Adoptionrequests a WHERE a.petid = :petid"),
    @NamedQuery(name = "Adoptionrequests.findByRequestdate", query = "SELECT a FROM Adoptionrequests a WHERE a.requestdate = :requestdate"),
    @NamedQuery(name = "Adoptionrequests.findByPetstatus", query = "SELECT a FROM Adoptionrequests a WHERE a.petstatus = :petstatus"),
    @NamedQuery(name = "Adoptionrequests.getAllRequests", query = "SELECT a FROM Pets p, Adoptionrequests a WHERE p.id = a.petid AND p.shelterEmail = :shelterEmail")})
public class Adoptionrequests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "clientemail")
    private String clientemail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "petid")
    private int petid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "requestdate")
    @Temporal(TemporalType.DATE)
    private Date requestdate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "petstatus")
    private String petstatus;
    @Transient
    private boolean enListaNegra;

    public Adoptionrequests() {
    }

    public Adoptionrequests(Integer id) {
        this.id = id;
    }

    public Adoptionrequests(Integer id, String clientemail, int petid, Date requestdate, String petstatus) {
        this.id = id;
        this.clientemail = clientemail;
        this.petid = petid;
        this.requestdate = requestdate;
        this.petstatus = petstatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientemail() {
        return clientemail;
    }

    public void setClientemail(String clientemail) {
        this.clientemail = clientemail;
    }

    public int getPetid() {
        return petid;
    }

    public void setPetid(int petid) {
        this.petid = petid;
    }

    public Date getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(Date requestdate) {
        this.requestdate = requestdate;
    }

    public String getPetstatus() {
        return petstatus;
    }

    public void setPetstatus(String petstatus) {
        this.petstatus = petstatus;
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
        if (!(object instanceof Adoptionrequests)) {
            return false;
        }
        Adoptionrequests other = (Adoptionrequests) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.uva.petadopt.entities.Adoptionrequests[ id=" + id + " ]";
    }
    
    public boolean isEnListaNegra() {
        return enListaNegra;
    }

    public void setEnListaNegra(boolean enListaNegra) {
        this.enListaNegra = enListaNegra;
    }
}
