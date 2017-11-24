package fr.polytech.unice;

import fr.polytech.unice.servlet.MainWorker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BronzeWorkerServlet extends MainWorker {

    public BronzeWorkerServlet()
    {
        super(0, 60000 * 5);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("worker bronze servlet");
    }
}