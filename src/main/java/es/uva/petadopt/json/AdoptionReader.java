/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.json;

import es.uva.petadopt.entities.Adoptionrequests;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author vcast
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class AdoptionReader implements MessageBodyReader<Adoptionrequests>{

    @Override
  public boolean isReadable(Class<?> cl, Type type, Annotation[] arg2, javax.ws.rs.core.MediaType arg3) {
    return Adoptionrequests.class.isAssignableFrom(cl);
  }

  @Override
  public String toString() {
    return "AdoptionReader []";
  }

  @Override
  public Adoptionrequests readFrom(Class<Adoptionrequests> type, 
          Type genericType, 
          Annotation[] annotations, 
          MediaType mediaType, 
          MultivaluedMap<String, 
          String> httpHeaders, 
          InputStream entityStream)
          throws IOException, WebApplicationException {
        
    Adoptionrequests request = new Adoptionrequests();
    JsonParser parser = Json.createParser(entityStream);
    
    SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        
    while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    String key = parser.getString();
                    parser.next();
                    switch (key) {
                        case "id":
                            request.setId(parser.getInt());
                            break;
                        case "clientemail":
                            request.setClientemail(parser.getString());
                            break;
                        case "petid":
                            request.setPetid(parser.getInt());
                            break;
                        case "requestdate":{
                            try {
                                request.setRequestdate(fecha.parse(parser.getString()));
                            } catch (ParseException ex) {
                                Logger.getLogger(AdoptionReader.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                            break;
                        case "petstatus":
                            request.setPetstatus(parser.getString());
                            break;
                        default:
                            break;
                    }
                    break;
                    
                default:
                    break;
            }
        }
        return request;
    }
    
}
