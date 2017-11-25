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


public class UploadVideo extends HttpServlet {

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    @Override public void doPost(HttpServletRequest req, HttpServletResponse result) throws IOException {
        result.setContentType("text/html");

        //get username ,videoName and duration  from request
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(req.getReader()).getAsJsonObject();

        String videoName = obj.get("videoName").getAsString();

        Double videoDuration = obj.get("videoDuration").getAsDouble();

        System.out.println("videoName : " + videoName);
        System.out.println("videoDuration : " + videoDuration);



        // Reserve place
        String original = videoName.toLowerCase() + "-" + UUID.randomUUID().toString();
        // String converted = videoName.toLowerCase() + "-" + UUID.randomUUID().toString();

        //her come the stoke of the original video

        /*********************************
         * stoke the video into google cloud storage
         *
         */

        // Write original file
        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", original);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, instance);
        InputStream stream = new ByteArrayInputStream(original.getBytes(StandardCharsets.UTF_8.name()));
        Util.copy(stream, Channels.newOutputStream(outputChannel));
        result.getWriter().println("video Original was save in cloud storage");

    }

}

