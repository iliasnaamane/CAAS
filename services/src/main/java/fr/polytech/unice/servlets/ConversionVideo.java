package fr.polytech.unice.servlets;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.tools.cloudstorage.*;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.Task;
import fr.polytech.unice.model.User;
import fr.polytech.unice.servlets.utils.Util;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;


public class ConversionVideo extends HttpServlet {



    @Override public void doPost(HttpServletRequest req, HttpServletResponse result) throws IOException {
        result.setContentType("text/html");

        //get username ,videoName and duration  from request
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(req.getReader()).getAsJsonObject();

        String username = obj.get("username").getAsString();
        String original = obj.get("original").getAsString();
        String format = obj.get("format").getAsString();


        System.out.println("username : " + username);
        System.out.println("videoName : " + original);
        System.out.println("format : " + format);


        List<User> users = ObjectifyService.ofy().load().type(User.class).filter(new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username)).list();

        Long id = null;

        if (!users.isEmpty()) {
            User user = users.get(0);
            if (user.username.equals(username)) {
                id = user.id;
            }
        }

        // Retrieve user data
        User user = ObjectifyService.ofy().load().key(Key.create(User.class, id)).now();
        if (user == null) {
            System.out.println("user not found ! ");
            result.getWriter().println("user not found ! ");
        } else {
            System.out.println("get user successfully");
            System.out.println(" user-id " + user.id);
            System.out.println("user-name " + user.username);
            System.out.println("user- emal " + user.mail);
            result.getWriter().println("user has been found successfully ");
        }


        // Reserve place
        String converted = user.username.toLowerCase() + "-" + UUID.randomUUID().toString();


        // Create a new task
        Task task = new Task(Key.create(User.class, user.id), original, converted, (format != null) ? format : "unknown");
        ObjectifyService.ofy().save().entity(task).now();
        result.getWriter().println("task has been created successfully ");

        // Enqueue task
        Queue queue;
        String url;
        switch (user.offer) {
            case User.BRONZE_OFFER:
                queue = QueueFactory.getQueue("bronze-queue");
                url = "/worker/bronze/";
                break;
            case User.SILVER_OFFER:
                queue = QueueFactory.getQueue("silver-queue");
                url = "/worker/silver/";
                break;
          /*  case User.GOLD_OFFER:
                queue = QueueFactory.getQueue("gold-queue");
                url = "/worker/gold";
                break;*/
            default:
                result.sendRedirect("/");
                return;
        }
        result.getWriter().println("queue change ");
        queue.add(TaskOptions.Builder.withUrl(url).method(TaskOptions.Method.POST).param("user", String.valueOf(user.id)).param("task", String.valueOf(task.id)).param("videoDuration", String.valueOf("12")));


        //send email
    }

}

