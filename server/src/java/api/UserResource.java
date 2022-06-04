/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import entity.User;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Collection;

/**
 *
 * @author jcvsa
 */
@Named // so that dependency injection can be used for the EJB
@Path("/users")
public class UserResource {

    @EJB
    private UserBean usersBean;

    public UserResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{user}")
    public String getUser(@PathParam("user") String username) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Collection<User> users = usersBean.getUser(username);
        for (User user : users) {
            arrayBuilder.add(user.getJSONObject());
        }
        JsonObject json = jsonBuilder.add("users", arrayBuilder).build();

        return json.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllJSONUsers() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Collection<User> allUsers = usersBean.getAllUsers();
        for (User user : allUsers) {
            arrayBuilder.add(user.getJSONObject());
        }
        JsonObject json = jsonBuilder.add("users", arrayBuilder).build();

        return json.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{username}")
    public void updateUser(MultivaluedMap<String, String> formParams, @PathParam("username") String username ) { 
        String name = formParams.getFirst("name");
        String email = formParams.getFirst("email");
        String password = formParams.getFirst("password");

        usersBean.updateUser(username, name, email, password);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewUser(MultivaluedMap<String, String> formParams) {
        String username = formParams.getFirst("username");
        String name = formParams.getFirst("name");
        String email = formParams.getFirst("email");
        String password = formParams.getFirst("password");
        usersBean.addUser(username, name, email, password);
    }
}
