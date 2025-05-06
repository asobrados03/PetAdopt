/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author alfre
 */
@Provider
public class ValidationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException ex) {
    StringBuilder sb = new StringBuilder("Errores de validación:\n");
    for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
      sb.append(v.getPropertyPath())
        .append(": ")
        .append(v.getMessage())
        .append("\n");
    }
    return Response.status(Response.Status.BAD_REQUEST)
                   .entity(sb.toString())
                   .type(MediaType.TEXT_PLAIN)
                   .build();
  }
}
