package fr.polytech.unice.servelets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.User;


public class CreateUserServelet extends HttpServlet {
  // Process the http POST of the form
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    User utilisateur;

    String username = req.getParameter("username");
    String mail = req.getParameter("mail");  
    int offer = Integer.parseInt(req.getParameter("offer"));

    utilisateur = new User(username,mail,offer);
  
    ObjectifyService.ofy().save().entity(utilisateur).now();
  }
}
//[END all]
