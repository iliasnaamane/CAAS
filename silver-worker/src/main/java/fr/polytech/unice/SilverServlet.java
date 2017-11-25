package fr.polytech.unice;

import fr.polytech.unice.servlet.MainWorker;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 04/11/17.
 */
public class SilverServlet extends MainWorker {
    public SilverServlet() {
        super(3, 60000 * 5);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("worker silver servlet");
    }
    
}
