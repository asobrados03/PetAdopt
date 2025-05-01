/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.json;

import es.uva.petadopt.entities.Pets;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author alfre
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class PetWriter implements MessageBodyWriter<Pets> {

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return Pets.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Pets t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return -1; // Método deprecado
    }

    @Override
    public void writeTo(Pets pet, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException {
        JsonGenerator gen = Json.createGenerator(entityStream);
        gen.writeStartObject()
                .write("id", pet.getId())
                .write("name", pet.getName())
                .write("species", pet.getSpecies())
                .write("breed", pet.getBreed())
                .write("age", pet.getAge())
                .write("health_status", pet.getHealthStatus())
                .write("adoption_cost", pet.getAdoptionCost())
                .write("shelter_name", pet.getShelterName())
                .writeEnd();
        gen.flush();
    }

}
