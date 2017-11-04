package fr.polytech.unice;

import fr.polytech.unice.servlet.WorkerServlet;
/**
 * Created by user on 04/11/17.
 */
public class SilverServlet extends WorkerServlet {
    public SilverServlet() {
        super(3, 60000 * 5);
    }
}
