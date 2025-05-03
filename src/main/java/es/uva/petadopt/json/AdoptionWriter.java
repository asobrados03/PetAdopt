/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.json;

import es.uva.petadopt.entities.AdoptionRequests;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author vcast
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AdoptionWriter implements MessageBodyWriter<AdoptionRequests>{

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return AdoptionRequests.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(AdoptionRequests t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return -1;
    }

    @Override
    public void writeTo(AdoptionRequests t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("clientemail: " + t.getClientEmail());
        System.out.println("pet id: " + t.getPetId());
        System.out.println("fecha: " + t.getRequestDate());
        System.out.println("status: " + t.getStatus());
        StringWriter sw = new StringWriter();
    JsonGenerator debugGen = Json.createGenerator(sw);
    debugGen.writeStartObject()
            .write("client_email", t.getClientEmail())
            .write("pet_id", t.getPetId())
            .write("request_date", fecha.format(t.getRequestDate()))
            .write("status", t.getStatus())
            .writeEnd();
    debugGen.close();
    System.out.println("JSON enviado: " + sw.toString());
        JsonGenerator gen = Json.createGenerator(entityStream);
        gen.writeStartObject()
                .write("client_email", t.getClientEmail())
                .write("pet_id", t.getPetId())
                .write("request_date",fecha.format(t.getRequestDate()))
               
                .writeEnd();
        gen.flush();
 
    }

  
    
}
