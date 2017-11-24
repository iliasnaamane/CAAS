package fr.polytech.unice.servlets;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.cloudstorage.*;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.User;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;


public class UserCreate extends HttpServlet {



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User utilisateur;


        resp.setContentType("text/html");
        Gson gson = new Gson();

        //get user from request
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(req.getReader()).getAsJsonObject();
        String username = obj.get("username").getAsString();
        String mail = obj.get("mail").getAsString();
        int offer = obj.get("offer").getAsInt();



        resp.getWriter().println("video Original was save in cloud storage");

        //verif if user exist
        List<User> users = ofy().load().type(User.class).filter(new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username)).list();
        if (users.isEmpty()) {
            utilisateur = new User(username, mail, offer);

            //store user in data store
            ObjectifyService.ofy().save().entity(utilisateur).now();
            String json = gson.toJson(utilisateur);
            resp.getWriter().println("user has been successfully created \n" +json);
        } else {
            resp.getWriter().println("can't create user because it already exists ! ");
        }
    }
}
