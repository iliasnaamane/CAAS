package fr.polytech.unice;


import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.common.primitives.Longs;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import fr.polytech.unice.model.Task;
import fr.polytech.unice.model.User;
import fr.polytech.unice.utils.Mail;
import fr.polytech.unice.utils.Util;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServlet;

public class BronzeWorkerServlet extends HttpServlet {

     private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("worker bronze servlet");
        response.setStatus(200);
    }
    

    @Override
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
        
        // Retrieve current task
         Task task = ObjectifyService.ofy().load().key(Key.create(Key.create(User.class, user.id), Task.class, Longs.tryParse(taskId))).now();
        
        // Retrieve all tasks
        List<Task> tasks = ObjectifyService.ofy().load().type(Task.class).ancestor(user).filter("state =", 0).order("-created").list();
        if (tasks.size() > 1) {
             // delete task from datastore
             ObjectifyService.ofy().delete().entity(task).now();
            response.setStatus(400);
            response.getWriter().println("Bronze can only convert one ");
           
            
        } else {
           
            task.state = Task.PENDING_STATE;
            ObjectifyService.ofy().save().entity(task).now();


            // Read original movie and remplace him by the converted Video
            GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
            GcsFilename originalFile = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", task.original);
            GcsFilename convertedFile = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", task.converted);
            GcsInputChannel inputChannel = gcsService.openReadChannel(originalFile, 0);
            
            // Process conversion
            try {
                Thread.sleep((long) Double.parseDouble(videoDuration));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            GcsOutputChannel outputChannel = gcsService.createOrReplace(convertedFile, instance);
            //InputStream stream = new ByteArrayInputStream(task.original.getBytes(StandardCharsets.UTF_8.name()));
            Util.copy(Channels.newInputStream(inputChannel), Channels.newOutputStream(outputChannel));

            //effacer ou pas l'original video??

            // Update state to done status
            task.state = Task.DONE_STATE;
            task.expired = new Date(System.currentTimeMillis() + 60000 * 5);
            ObjectifyService.ofy().save().entity(task).now();

            //send mail that the converson is done

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