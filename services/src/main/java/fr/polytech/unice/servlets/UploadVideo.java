package fr.polytech.unice.servlets;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.Task;
import fr.polytech.unice.model.User;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class UploadVideo extends HttpServlet {



    @Override public void doPost(HttpServletRequest req, HttpServletResponse result) throws IOException {
        result.setContentType("text/html");

        //get username ,videoName and duration  from request
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(req.getReader()).getAsJsonObject();

        String username = obj.get("username").getAsString();
        String videoName = obj.get("videoName").getAsString();
        String format = obj.get("format").getAsString();
        Double videoDuration = obj.get("videoDuration").getAsDouble();

        System.out.println("username : " + username);
        System.out.println("videoName : " + videoName);
        System.out.println("videoDuration : " + videoDuration);
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
        String original = user.username.toLowerCase() + "-" + UUID.randomUUID().toString();
        String converted = user.username.toLowerCase() + "-" + UUID.randomUUID().toString();

        //her come the stoke of the original video


        // Create a new task
        Task task = new Task(Key.create(User.class, user.id), original, converted, (format != null) ? format : "unknown");
        ObjectifyService.ofy().save().entity(task).now();
        result.getWriter().println("task has been create successfully ");

        // Enqueue task
        Queue queue;
        String url;
        switch (user.offer) {
            case User.BRONZE_OFFER:
                queue = QueueFactory.getQueue("bronze-queue");
                url = "/worker/bronze";
                break;
            case User.SILVER_OFFER:
                queue = QueueFactory.getQueue("silver-queue");
                url = "/worker/silver";
                break;
          /*  case User.GOLD_OFFER:
                queue = QueueFactory.getQueue("gold-queue");
                url = "/worker/gold";
                break;*/
            default:
                result.sendRedirect("/");
                return;
        }
        queue.add(TaskOptions.Builder.withUrl(url).method(TaskOptions.Method.POST).param("user", String.valueOf(user.id)).param("task", String.valueOf(task.id)));


        //send email
    }

}

