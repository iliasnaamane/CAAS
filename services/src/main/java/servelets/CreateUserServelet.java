package servelets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import model.User;

public class CreateUserServelet extends HttpServlet {
  // Process the http POST of the form
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    User utilisateur;
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();  

    String username = req.getParameter("username");
    String mail = req.getParameter("mail");  
    String offer = req.getParameter("offer");

    utilisateur = new User(username, mail, offer);
  
    ObjectifyService.ofy().save().entity(utilisateur).now();
  }
}
//[END all]
