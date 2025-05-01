/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.json;

import es.uva.petadopt.entities.Pets;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author alfre
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class PetReader implements MessageBodyReader<Pets> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return Pets.class.isAssignableFrom(type);
    }

    @Override
    public Pets readFrom(Class<Pets> type,
            Type genericType,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream)
            throws IOException, WebApplicationException {

        Pets pet = new Pets();
        JsonParser parser = Json.createParser(entityStream);

        while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    String key = parser.getString();
                    parser.next();
                    switch (key) {
                        case "id":
                            pet.setId(parser.getInt());
                            break;
                        case "name":
                            pet.setName(parser.getString());
                            break;
                        case "species":
                            pet.setSpecies(parser.getString());
                            break;
                        case "breed":
                            pet.setBreed(parser.getString());
                            break;
                        case "age":
                            pet.setAge(parser.getInt());
                            break;
                        case "health_status":
                            pet.setHealthStatus(parser.getString());
                            break;
                        case "adoption_cost":
                            pet.setAdoptionCost(parser.getBigDecimal()); // Usa BigDecimal para DECIMAL  
                            break;
                        case "shelter_name":
                            pet.setShelterName(parser.getString());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        return pet;
    }

}
