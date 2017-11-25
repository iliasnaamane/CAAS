package fr.polytech.unice;

import fr.polytech.unice.servlet.MainWorker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 25/11/17.
 */
public class GoldServlet extends MainWorker {

    public GoldServlet() {
        super(5, 60000 * 10);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("worker gold servlet");
    }
}
