package de.tum.in.eist;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Manages user accounts in google Datastore
 */
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Path("/user")
public class UserAccountManager {

  /**
   * Retrieves the user information from Datastore using
   * userName is key
   * @return JSON of {@link UserData} POJO
   */
  @GET
  @Path("info")
  public Response getUserInfo(@Context HttpServletRequest request) {
    
    // get the userName (Google Email Id) of the current user here using UserService
    // For now we are using static data in the skeleton
    String userName = "abc@gmail.com";
    
    // retrieves the data from datastore using Objectify (userName as key)    
    UserData user = ofy().load().type(UserData.class).id(userName).now();
    if (user.getUserId() == null) {
      return Response.serverError().build();
    } else {
      return Response.ok(user).build();
    }
  }

  /**
   * Retrieves the logout URL of signed in user using UserService API
   * @return logout URL in plain text
   */
  @GET
  @Path("userLogout")
  @Produces("plain/text; charset=UTF-8")
  public Response getLogoutURL(@Context HttpServletRequest request) {
    // get the logout url of the current user here using UserService API
    // For now we are just using link of google page.
    String logoutUri = "http://www.google.de";
    return Response.ok(logoutUri).build();
  }

  /**
   * Saves new user in Datastore.
   * Retrives the userName(Google Email Id) of the current user using UserService API
   */
  @POST
  public UserData addUser(UserData user, @Context HttpServletRequest request) {
    // get the userName (Google Email Id) of the current user here using UserService
    // For now we are using static data in the skeleton
    String userName = "abc@gmail.com";
    
    // set the userName of user to the email Id given by UserService
    user.setUserId(userName);
    
    // Save the user profile in datastore using Objectify
    ofy().save().entity(user);
    return user;
  }
}
