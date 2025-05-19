/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Clase para gestionar el nombre del refugio
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {
    private String shelterName;
    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }
}
