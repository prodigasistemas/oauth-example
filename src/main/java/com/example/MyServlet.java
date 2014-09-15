package com.example;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/servlet"})
public class MyServlet extends HttpServlet {
	
	@Inject
	private CDIResource res;
	
    protected void processRequest(HttpServletRequest httpRequest, HttpServletResponse response)
            throws ServletException, IOException {
    	DataOutputStream out = new DataOutputStream(response.getOutputStream());
    	out.writeChars("OK " + res.getTexto());
    	out.flush();
    	out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
