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
 * Clase para validar el telefono
 *
 * @authors: Víctor Castrillo y Alfredo Sobrados
 */
@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator {
    
    private static final String PHONE_PATTERN = "^\\+?[0-9]{9,13}$";
    private Pattern pattern;
    
    public PhoneValidator() {
        pattern = Pattern.compile(PHONE_PATTERN);
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        if (value == null) {
            return;
        }
        
        if (!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error de validación", value + " no es un número de teléfono válido."));
        }
    }
}
