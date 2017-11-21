package fr.polytech.unice;

import fr.polytech.unice.servlet.MainWorker;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by user on 04/11/17.
 */

public class BronzeServlet extends HttpServlet {
    
    private MainWorker mw;
    public BronzeServlet() {
         mw = new MainWorker(0, 60000);
    }
    
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mw.doPost(request, response);
    }
    
    
   
    
}
