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
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet gets the input stream for a given msg part and 
 * pushes it out to the browser with the correct content type.
 * Used to display attachments and relies on the browser's
 * content handling capabilities.
 */
public class AttachmentServlet extends HttpServlet {

    /**
     * This method handles the GET requests from the client.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse  response)
	throws IOException, ServletException {
      
	HttpSession session = request.getSession();
	ServletOutputStream out = response.getOutputStream();
	int msgNum = Integer.parseInt(request.getParameter("message"));
	int partNum = Integer.parseInt(request.getParameter("part"));
	MailUserBean mailuser = (MailUserBean)session.getAttribute("mailuser");

	// check to be sure we're still logged in
	if (mailuser.isLoggedIn()) {
	    try {
		Message msg = mailuser.getFolder().getMessage(msgNum);

		Multipart multipart = (Multipart)msg.getContent();
		Part part = multipart.getBodyPart(partNum);
		
		String sct = part.getContentType();
		if (sct == null) {
		    out.println("invalid part");
		    return;
		}
		ContentType ct = new ContentType(sct);

		response.setContentType(ct.getBaseType());
		InputStream is = part.getInputStream();
		int i;
		while ((i = is.read()) != -1)
		    out.write(i);
		out.flush();
		out.close();

	    } catch (MessagingException ex) {
		throw new ServletException(ex.getMessage());
	    }
	} else {
	    getServletConfig().getServletContext().
		getRequestDispatcher("/index.html").
		forward(request, response);
	}
    }   
}
