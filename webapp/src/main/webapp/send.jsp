<%--

    Copyright (c) 2001, 2019 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ taglib uri="https://github.com/eclipse-ee4j/jakartamail/tree/master/webapp" 
    prefix="jakartamail" %>
 
<html>
<head>
	<title>Jakarta Mail send</title>
</head>
	
<body bgcolor="white">
<jakartamail:sendmail 
   recipients="<%= request.getParameter(\"to\") %>"
   sender="<%= request.getParameter(\"from\") %>"
   subject="<%= request.getParameter(\"subject\") %>"
>
<%= request.getParameter("text") %>
</jakartamail:sendmail>
	    
<h1>Message sent successfully</h1>
	
</body>
</html>

