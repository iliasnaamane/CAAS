package fr.polytech.unice.model;

import com.googlecode.objectify.ObjectifyService;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a everything.
 */
public class OfyHelper implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Task.class);
        ObjectifyService.register(Video.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }

}