/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.jaas;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Clase para validar el nif
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@FacesValidator("nifValidator")
public class NifValidator implements Validator {
    
    private static final String NIF_PATTERN = "^[0-9]{8}[A-Z]$";
    private Pattern pattern;
    
    public NifValidator() {
        pattern = Pattern.compile(NIF_PATTERN);
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        if (value == null) {
            return;
        }
        
        String nif = value.toString().toUpperCase();
        
        if (!pattern.matcher(nif).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error de validación", value + " no es un NIF válido."));
        }
        
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int resto = Integer.parseInt(nif.substring(0, 8)) % 23;
        char letraCalculada = letras.charAt(resto);
        char letraProporcionada = nif.charAt(8);
        
        if (letraCalculada != letraProporcionada) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error de validación", "La letra del NIF no es correcta."));
        }
    }
}
