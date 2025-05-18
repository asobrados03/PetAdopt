/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.json;

import es.uva.petadopt.entities.Adoptionrequests;
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
public class AdoptionWriter implements MessageBodyWriter<Adoptionrequests> {

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return Adoptionrequests.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Adoptionrequests t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return -1;
    }

    @Override
    public void writeTo(Adoptionrequests t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        JsonGenerator gen = Json.createGenerator(entityStream);
        gen.writeStartObject()
                .write("clientemail", t.getClientemail())
                .write("petid", t.getPetid())
                .write("requestdate", fecha.format(t.getRequestdate()))
                .write("petstatus", t.getPetstatus())
                .writeEnd();
        gen.flush();

    }

}
