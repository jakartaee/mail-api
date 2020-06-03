/*
 * Copyright (c) 2001, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package demo;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 * This servlet is used to determine whether the user is logged in before
 * forwarding the request to the selected URL.
 */
public class FilterServlet extends HttpServlet {

    /**
     * This method handles the "POST" submission from two forms: the
     * login form and the message compose form.
     */
    public void doPost(HttpServletRequest request, 
		       HttpServletResponse  response) 
		       throws IOException, ServletException {

	String servletPath = request.getServletPath();
	servletPath = servletPath.concat(".jsp");
	
	getServletConfig().getServletContext().
	    getRequestDispatcher("/" + servletPath).forward(request, response);
    }

    /**
     * This method handles the GET requests from the client.
     */
    public void doGet(HttpServletRequest request, 
		      HttpServletResponse  response)
		      throws IOException, ServletException {
      
	// check to be sure we're still logged in 
	// before forwarding the request.
	HttpSession session = request.getSession();
	MailUserBean mailuser = (MailUserBean)session.getAttribute("mailuser");
	String servletPath = request.getServletPath();
	servletPath = servletPath.concat(".jsp");
	
	if (mailuser.isLoggedIn())
	    getServletConfig().getServletContext().
		getRequestDispatcher("/" + servletPath).
		forward(request, response);
	else
	    getServletConfig().getServletContext().
		getRequestDispatcher("/index.html").
		forward(request, response);
    }
}

