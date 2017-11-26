/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.polytech.unice.utils;

import com.google.appengine.api.taskqueue.TaskOptions;
import fr.polytech.unice.model.Task;
import fr.polytech.unice.model.User;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.googlecode.objectify.ObjectifyService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Silver Handler class
 */
public class SilverHandler {
    private static final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    
    
    public static void handleTask(Task task, Queue q, User user, int duration, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException{
       
        List<Task> tasks = ObjectifyService.ofy().load().type(Task.class).ancestor(user).filter("state =", 0).order("-created").list();
        if(tasks.size() >= 4){
            ObjectifyService.ofy().delete().entity(task).now();
            response.setStatus(400);
            response.getWriter().println("Over quota of 3");
            
        } 
        else {
            q.add(
              TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload("some content").tag(user.username.getBytes())
              );               
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
            
            
            
            List<TaskHandle> taskss = q.leaseTasksByTag(3600, TimeUnit.SECONDS, tasks.size()+1,user.username);
            String message = processTasks(taskss, q);
            
            response.getWriter().println(message);
            // Update state to done status
            task.state = Task.DONE_STATE;
            task.expired = new Date(System.currentTimeMillis() + 60000);
            ObjectifyService.ofy().save().entity(task).now();
            
            
            
            
               
        }
       
    }
    
    
    
    private static String processTasks(List<TaskHandle> tasks, Queue q) throws InterruptedException {
        String payload; String message;
        int numberOfDeletedTasks = 0;
        for (TaskHandle task : tasks) {
            payload = new String(task.getPayload());
            
            String output
                    = String.format("Processing: taskName='%s'  payload='%s'",
                            task.getName(), payload);
            Thread.sleep(1000);
            output = String.format("Deleting taskName='%s'", task.getName());
          
            // [START delete_task]
            q.deleteTask(task);
            // [END delete_task]
            numberOfDeletedTasks++;
        }
        if (numberOfDeletedTasks > 0) {
            message
                    = "Processed and deleted  tasks from the  task queue."+numberOfDeletedTasks;
        } else {
            message = "Task Queue has no tasks available for lease.";
        }
        
        
        return message;
    }

}
