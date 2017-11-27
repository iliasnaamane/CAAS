package fr.polytech.unice.servlets;

import com.google.appengine.tools.cloudstorage.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.polytech.unice.servlets.utils.Util;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.channels.Channels;

import java.nio.charset.StandardCharsets;

import java.util.UUID;


public class UploadVideo extends HttpServlet {

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    @Override public void doPost(HttpServletRequest req, HttpServletResponse result) throws IOException {
        result.setContentType("text/html");

        //get videoName and duration  from request
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(req.getReader()).getAsJsonObject();

        String videoName = obj.get("videoName").getAsString();

        Double videoDuration = obj.get("videoDuration").getAsDouble();

        System.out.println("videoName : " + videoName);
        System.out.println("videoDuration : " + videoDuration);

        
        

        // Reserve place for original video
       // String original = videoName;
        String original = videoName.toLowerCase() + "-" + UUID.randomUUID().toString();

        char[] data = new char[1024*1024*videoDuration.intValue()];
        String fileContent = new String(data);
        //store the original video into google cloud storage

        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename("staging.sacc-belhassen-182811.appspot.com", original);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, instance);
        InputStream stream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8.name()));
        Util.copy(stream, Channels.newOutputStream(outputChannel));
        result.getWriter().println("video Original was save in cloud storage : ");
        result.getWriter().println(original);

    }

}

