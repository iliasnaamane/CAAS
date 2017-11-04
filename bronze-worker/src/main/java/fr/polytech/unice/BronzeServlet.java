package fr.polytech.unice;

import fr.polytech.unice.servlet.WorkerServlet;

/**
 * Created by user on 04/11/17.
 */
public class BronzeServlet extends WorkerServlet {
    public BronzeServlet() {
        super(0, 60000 * 5);
    }
}
