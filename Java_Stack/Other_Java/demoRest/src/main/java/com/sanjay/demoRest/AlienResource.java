package com.sanjay.demoRest;

import java.sql.SQLException;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("aliens")
public class AlienResource {

    private final AlienRepository repo = new AlienRepository();

    @GET
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public List<Alien> getAliens() throws SQLException {
        return repo.getAliens();
    }

    @GET
    @Path("alien/{id}")
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Alien getAlien(@PathParam("id") int id) throws SQLException {
        return repo.getAlien(id);
    }

    @POST
    @Path("alien")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Alien createAlien(Alien alien) throws SQLException {
        System.out.println(alien);

        repo.create(alien);

        return alien;
    }
    
    @PUT
    @Path("alien")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Alien updateAlien(Alien alien) throws SQLException {
        System.out.println(alien);                                       
        if(repo.getAlien(alien.getId())==null){
        	repo.create(alien);
        }else{
        	repo.update(alien);
        }
        return alien;
    }
    
    @DELETE
    @Path("alien/{id}")
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Alien killAlien(@PathParam("id")int id) throws SQLException {
    	Alien a = repo.getAlien(id);
    	
    	if(a.getId()!=0) {
    		repo.delete(id);
    	}
		return a;
    }
}