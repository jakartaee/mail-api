<%--

    Copyright (c) 2001, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ taglib uri="http://java.sun.com/products/javamail/demo/webapp" 
    prefix="javamail" %>
 
<html>
<head>
	<title>JavaMail send</title>
</head>
	
<body bgcolor="white">
<javamail:sendmail 
   recipients="<%= request.getParameter(\"to\") %>"
   sender="<%= request.getParameter(\"from\") %>"
   subject="<%= request.getParameter(\"subject\") %>"
>
<%= request.getParameter("text") %>
</javamail:sendmail>
	    
<h1>Message sent successfully</h1>
	
</body>
</html>

