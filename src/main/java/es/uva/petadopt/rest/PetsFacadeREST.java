/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uva.petadopt.rest;

import es.uva.petadopt.entities.Pets;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author alfre
 */
@Named
@Stateless
@Path("es.uva.petadopt.entities.pets")
public class PetsFacadeREST extends AbstractFacade<Pets> {

    @PersistenceContext(unitName = "es.uva.petadopt_PetAdopt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    
    public PetsFacadeREST() {
        super(Pets.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void create(Pets entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Integer id, Pets entity) {
        // 1) Forzamos el id (por si acaso no venía en el JSON)
        entity.setId(id);

        try {
            super.edit(entity);           // esto puede lanzar EJBException
            return Response.noContent().build();
        } catch (EJBException ejbEx) {
            // 2) Desenvuelve hasta encontrar el ConstraintViolationException
            Throwable t = ejbEx.getCause();
            while (t != null && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // 3) relanza la excepción pura para que tu mapper la coja
                throw (ConstraintViolationException) t;
            }
            // si no era un C.V.E. legítimo, vuelve a lanzar el EJBException
            throw ejbEx;
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Pets find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("shelter/{shelterEmail}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Pets> findByShelter(@PathParam("shelterEmail") String shelterEmail) {
        return em.createQuery(
                "SELECT p FROM Pets p WHERE p.shelterEmail = :sn", Pets.class)
                .setParameter("sn", shelterEmail)
                .getResultList();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Pets> findAll(@QueryParam("shelter") String shelterEmail) {
        if (shelterEmail != null) {
            return em.createQuery(
                    "SELECT p FROM Pets p WHERE p.shelterEmail = :se", Pets.class)
                    .setParameter("se", shelterEmail)
                    .getResultList();
        }
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Pets> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
