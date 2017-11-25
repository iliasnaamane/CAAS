package fr.polytech.unice.servlet;



import com.google.appengine.tools.cloudstorage.*;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Longs;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.Task;
import fr.polytech.unice.model.User;
import fr.polytech.unice.utils.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import fr.polytech.unice.utils.Mail;


public class MainWorker extends HttpServlet {


    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    // Concurrent policy
    private int concurrent;

    // Expiration policy
    private long expiration;

    public MainWorker(int concurrent, long expiration) {
        this.concurrent = concurrent;
        this.expiration = expiration;
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("worker bronze servlet");
        response.setStatus(200);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve task & user from request
        String userId = request.getParameter("user");
        String taskId = request.getParameter("task");
        String videoDuration = request.getParameter("videoDuration");

        // Retrieve user from datastore
        User user = ObjectifyService.ofy().load().key(Key.create(User.class, Longs.tryParse(userId))).now();
        if (user == null) {
            System.out.println("user not found from worker");
            return;
        }

        // Retrieve all tasks
        List<Task> tasks = ObjectifyService.ofy().load().type(Task.class).ancestor(user).filter("state =", 0).order("-created").list();
        if (tasks.size() >= concurrent && concurrent > 0) {
            response.setStatus(400);
            response.getWriter().println("Over quota of " + concurrent);
        } else {
            // Retrieve current task
            Task task = ObjectifyService.ofy().load().key(Key.create(Key.create(User.class, user.id), Task.class, Longs.tryParse(taskId))).now();
            task.state = Task.PENDING_STATE;
            ObjectifyService.ofy().save().entity(task).now();


            // Read original movie and remplace him by the converted Video
            GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
            GcsFilename originalFile = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", task.original);
            GcsFilename convertedFile = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", task.converted);
            GcsInputChannel inputChannel = gcsService.openReadChannel(originalFile, 0);
            GcsOutputChannel outputChannel = gcsService.createOrReplace(convertedFile, instance);
            InputStream stream = new ByteArrayInputStream(task.original.getBytes(StandardCharsets.UTF_8.name()));
            Util.copy(stream, Channels.newOutputStream(outputChannel));

            // Process convcersion
            try {
                Thread.sleep((long) Double.parseDouble(videoDuration));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            //efacer ou pas l'original video??

            // Update state to done status
            task.state = Task.DONE_STATE;
            task.expired = new Date(System.currentTimeMillis() + expiration);
            ObjectifyService.ofy().save().entity(task).now();
            System.out.println("finally your task is done " + task.state);
            response.setContentType("text/html");
            response.getWriter().println("doneeeeee");

            //send mail that the vonverson is done

            response.getWriter().println("this mail inform you the video conversion is done.");
            Mail mail = new Mail();
            try {
              mail.sendSimpleMail(user.username ,user.mail,task.converted);
            }catch (Exception e){
              response.setContentType("text/plain");
              response.getWriter().println("the mail after conversion not done.");
            }

        }
    }

}
